package com.manyun.business.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.manyun.business.domain.entity.Box;
import com.manyun.business.domain.entity.CntCollection;
import com.manyun.business.domain.entity.CntConsignment;
import com.manyun.business.domain.entity.Order;
import com.manyun.business.domain.form.UserConsignmentForm;
import com.manyun.business.domain.query.ConsignmentQuery;
import com.manyun.business.domain.vo.ConsignmentBoxListVo;
import com.manyun.business.domain.vo.ConsignmentCollectionListVo;
import com.manyun.business.mapper.CntConsignmentMapper;
import com.manyun.business.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.comm.api.RemoteBuiUserService;
import com.manyun.common.core.constant.BusinessConstants;
import com.manyun.common.core.constant.SecurityConstants;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.exception.ServiceException;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.manyun.common.core.enums.ConsignmentStatus.*;

/**
 * <p>
 * 寄售市场主表 服务实现类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-30
 */
@Service
public class CntConsignmentServiceImpl extends ServiceImpl<CntConsignmentMapper, CntConsignment> implements ICntConsignmentService {


    @Autowired
    private IUserBoxService userBoxService;

    @Autowired
    private IUserCollectionService userCollectionService;

    @Autowired
    private ISystemService systemService;

    @Autowired
    private IBoxService boxService;

    @Autowired
    private ICollectionService collectionService;

    @Autowired
    private RemoteBuiUserService remoteBuiUserService;

    @Autowired
    private IOrderService orderService;


    /**
     * 资产挂卖寄售市场
     * @param userConsignmentForm
     * @param userId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void consignmentAssets(UserConsignmentForm userConsignmentForm, String userId) {
        //判定 用户是否有这个  盲盒 | 藏品
        checkAll(userId,userConsignmentForm);
        //将对应的藏品 或者 盲盒 更改状态,推到寄售市场 售卖
        aspect(userConsignmentForm,userId);

    }

    /**
     * 分页查询寄售市场藏品的信息
     * @param consignmentQuery
     * @return
     */
    @Override
    public TableDataInfo<ConsignmentCollectionListVo> pageConsignmentList(ConsignmentQuery consignmentQuery) {
        PageHelper.startPage(consignmentQuery.getPageNum(),consignmentQuery.getPageSize());

        LambdaQueryWrapper<CntConsignment> lambdaQueryWrapper = getCntConsignmentLambdaQueryWrapper(consignmentQuery,BusinessConstants.ModelTypeConstant.COLLECTION_TAYPE);
        List<CntConsignment> cntConsignments = list(lambdaQueryWrapper);
        // 封装组合数据
        return TableDataInfoUtil.pageTableDataInfo(encapsulationConsignmentListVo(cntConsignments),cntConsignments);
    }

    /**
     * 寄售市场的列表查询  藏品 盲盒子 封装条件
     * @param consignmentQuery
     * @param isType
     * @return
     */
    private LambdaQueryWrapper<CntConsignment> getCntConsignmentLambdaQueryWrapper(ConsignmentQuery consignmentQuery,Integer isType) {
        LambdaQueryWrapper<CntConsignment> lambdaQueryWrapper = Wrappers.<CntConsignment>lambdaQuery();
        // 硬性条件查询
        lambdaQueryWrapper.eq(CntConsignment::getIsType,isType);
        lambdaQueryWrapper.ne(CntConsignment::getConsignmentStatus, OVER_CONSIGN.getCode());
        // 相关条件查询
        lambdaQueryWrapper.eq(StrUtil.isNotBlank(consignmentQuery.getCateId()), CntConsignment::getCateId, consignmentQuery.getCateId());
        lambdaQueryWrapper.like(StrUtil.isNotBlank(consignmentQuery.getCommName()), CntConsignment::getBuiName, consignmentQuery.getCommName());
        // 相关排序判定 全部按照 倒序排序
        if (Integer.valueOf(1).equals(consignmentQuery.getIsNew()))
            lambdaQueryWrapper.orderByDesc(CntConsignment::getUpdatedTime);

        if (Objects.nonNull(consignmentQuery.getPriceOrder()) && Integer.valueOf(0).equals(consignmentQuery.getPriceOrder()))
            lambdaQueryWrapper.orderByDesc(CntConsignment::getConsignmentPrice);
        if (Objects.nonNull(consignmentQuery.getPriceOrder()) && Integer.valueOf(1).equals(consignmentQuery.getPriceOrder()))
            lambdaQueryWrapper.orderByAsc(CntConsignment::getConsignmentPrice);

        if (Objects.nonNull(consignmentQuery.getTimeOrder()) && Integer.valueOf(0).equals(consignmentQuery.getTimeOrder()))
            lambdaQueryWrapper.orderByDesc(CntConsignment::getCreatedTime);
        if (Objects.nonNull(consignmentQuery.getTimeOrder()) && Integer.valueOf(1).equals(consignmentQuery.getTimeOrder()))
            lambdaQueryWrapper.orderByAsc(CntConsignment::getCreatedTime);
        return lambdaQueryWrapper;
    }

    /**
     * 分页查询寄售市场盲盒的信息
     * @param consignmentQuery
     * @return
     */
    @Override
    public TableDataInfo<ConsignmentBoxListVo> pageConsignmentBoxList(ConsignmentQuery consignmentQuery) {
        LambdaQueryWrapper<CntConsignment> lambdaQueryWrapper = getCntConsignmentLambdaQueryWrapper(consignmentQuery,BusinessConstants.ModelTypeConstant.BOX_TAYPE);
        List<CntConsignment> cntConsignments = list(lambdaQueryWrapper);
        // 组合数据
        return TableDataInfoUtil.pageTableDataInfo(encapsulationBoxListVo(cntConsignments),cntConsignments);
    }

    private List<ConsignmentBoxListVo> encapsulationBoxListVo(List<CntConsignment> cntConsignments) {
        return cntConsignments.parallelStream().map(this::initBoxListVo).collect(Collectors.toList());
    }

    private ConsignmentBoxListVo initBoxListVo(CntConsignment cntConsignment) {
        ConsignmentBoxListVo consignmentBoxListVo = Builder.of(ConsignmentBoxListVo::new).build();
        consignmentBoxListVo.setBoxListVo(boxService.getBaseBoxListVo(cntConsignment.getRealBuiId()));
        consignmentBoxListVo.setCntUserDto(remoteBuiUserService.commUni(cntConsignment.getSendUserId(),  SecurityConstants.INNER).getData());
        consignmentBoxListVo.setId(cntConsignment.getId());
        consignmentBoxListVo.setConsignmentStatus(cntConsignment.getConsignmentStatus());
        consignmentBoxListVo.setCreatedTime(cntConsignment.getCreatedTime());
        // 需要验证订单 才可以拿到此值
        if (LOCK_CONSIGN.getCode().equals(cntConsignment.getConsignmentStatus()) && StrUtil.isNotBlank(cntConsignment.getOrderId())){
            // 订单查询 将剩余支付时间补足即可
            Order order = orderService.getById(cntConsignment.getOrderId());
            consignmentBoxListVo.setEndPayTime(order.getEndTime());
        }

        return consignmentBoxListVo;
    }

    private List<ConsignmentCollectionListVo> encapsulationConsignmentListVo(List<CntConsignment> cntConsignments) {
        if (cntConsignments.isEmpty()) return Collections.emptyList();
        return cntConsignments.parallelStream().map(this::initCollectionListVo).collect(Collectors.toList());
    }

    /**
     * 列表数据组合使用
     * @param cntConsignment
     * @return
     */
    private ConsignmentCollectionListVo initCollectionListVo(CntConsignment cntConsignment) {
        ConsignmentCollectionListVo collectionListVo = Builder.of(ConsignmentCollectionListVo::new).build();
        collectionListVo.setCollectionVo(collectionService.getBaseCollectionVo(cntConsignment.getRealBuiId()));
        collectionListVo.setCntUserDto(remoteBuiUserService.commUni(cntConsignment.getSendUserId(),  SecurityConstants.INNER).getData());
        collectionListVo.setId(cntConsignment.getId());
        collectionListVo.setConsignmentStatus(cntConsignment.getConsignmentStatus());
        collectionListVo.setCreatedTime(cntConsignment.getCreatedTime());
        // 需要验证订单 才可以拿到此值
        if (LOCK_CONSIGN.getCode().equals(cntConsignment.getConsignmentStatus()) && StrUtil.isNotBlank(cntConsignment.getOrderId())){
            // 订单查询 将剩余支付时间补足即可
            Order order = orderService.getById(cntConsignment.getOrderId());
            collectionListVo.setEndPayTime(order.getEndTime());
        }

        return collectionListVo;
    }

    /**
     * 对对应的 藏品 | 盲盒进行 寄售
     * @param userConsignmentForm
     * @param userId
     */
    private void aspect(@NotNull UserConsignmentForm userConsignmentForm,@NotNull String userId) {
        // 统一逻辑操作,将自己的藏品,盲盒 取消，推到寄售市场中寄售。
        Integer type = userConsignmentForm.getType();
        String buiId = userConsignmentForm.getBuiId();
        String realBuiId = null;
        String info = null;
        String cateId = null;
        String buiName= null;
        // 藏品
        if (BusinessConstants.ModelTypeConstant.COLLECTION_TAYPE.equals(type)){
            // 将自己的藏品隐藏,归并状态以及词条
             info = StrUtil.format("该藏品被寄售了,已将藏品移送到寄售市场!");
            realBuiId =  userCollectionService.hideUserCollection(buiId,userId,info);
            CntCollection cntCollection = collectionService.getById(realBuiId);
            cateId = cntCollection.getCateId();
            buiName = cntCollection.getCollectionName();
            return;
        }
       // 盲盒
        if (BusinessConstants.ModelTypeConstant.BOX_TAYPE.equals(type)){
            // 将自己的盲盒 隐藏,归并状态及词条
             info = StrUtil.format("该盲盒被寄售了,已将盲盒移送到寄售市场!");
            realBuiId = userBoxService.hideUserBox(buiId,userId,info);
            Box box = boxService.getById(realBuiId);
            cateId = box.getCateId();
            buiName= box.getBoxTitle();
            return;
        }
        if (StrUtil.isBlank(info) && StrUtil.isBlank(realBuiId))
           throw new ServiceException("not fount type [0-1] now type is "+type+"");

        pushConsignment(userId,userConsignmentForm.getConsignmentMoney(), type, userConsignmentForm.getBuiId(), realBuiId, info,cateId,buiName);

    }

    private void checkAll(String userId, UserConsignmentForm userConsignmentForm) {
        Integer type = userConsignmentForm.getType();
        // 藏品
        if (BusinessConstants.ModelTypeConstant.COLLECTION_TAYPE.equals(type))
            Assert.isTrue(userCollectionService.existUserCollection(userId,userConsignmentForm.getBuiId()),"选择的藏品有误,请核实藏品详细信息!");

        // 盲盒
        if (BusinessConstants.ModelTypeConstant.BOX_TAYPE.equals(type))
            Assert.isTrue(userBoxService.existUserBox(userId,userConsignmentForm.getBuiId()),"选择的盲盒有误,请核实盲盒详细信息!");
    }


    /**
     * 推送到交易市场
     */
    private void pushConsignment(String userId, BigDecimal consignmentPrice , Integer type, String buiId, String realBuiId,String info,String cateId,String buiName){
        CntConsignment cntConsignment = Builder.of(CntConsignment::new).build();
        cntConsignment.setId(IdUtil.getSnowflakeNextIdStr());
        cntConsignment.setConsignmentPrice(consignmentPrice);
        // 寄售手续费
        BigDecimal systemServiceVal = systemService.getVal(BusinessConstants.SystemTypeConstant.CONSIGNMENT_SERVER_CHARGE, BigDecimal.class);
        // 按照百分比计算
/*        cntConsignment.setServerCharge(NumberUtil.add(0D));
        if (systemServiceVal.compareTo(NumberUtil.add(0D)) >=1)*/
        cntConsignment.setServerCharge(consignmentPrice.multiply(systemServiceVal).divide(NumberUtil.add(100D),2,BigDecimal.ROUND_DOWN));
        cntConsignment.setConsignmentStatus(PUSH_CONSIGN.getCode());
        cntConsignment.setBuiId(buiId);
        cntConsignment.setSendUserId(userId);
        cntConsignment.setRealBuiId(realBuiId);
        cntConsignment.setIsType(type);
        cntConsignment.createD(userId);
        cntConsignment.setCateId(cateId);
        cntConsignment.setFormInfo(info);
        cntConsignment.setBuiName(buiName);
        save(cntConsignment);
    }


}
