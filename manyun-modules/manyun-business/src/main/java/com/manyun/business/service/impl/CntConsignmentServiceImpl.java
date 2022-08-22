package com.manyun.business.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.manyun.business.design.pay.RootPay;
import com.manyun.business.domain.dto.MsgCommDto;
import com.manyun.business.domain.dto.MsgThisDto;
import com.manyun.business.domain.dto.OrderCreateDto;
import com.manyun.business.domain.dto.PayInfoDto;
import com.manyun.business.domain.entity.*;
import com.manyun.business.domain.form.ConsignmentSellForm;
import com.manyun.business.domain.form.UserConsignmentForm;
import com.manyun.business.domain.query.ConsignmentOrderQuery;
import com.manyun.business.domain.query.ConsignmentQuery;
import com.manyun.business.domain.vo.*;
import com.manyun.business.mapper.CntConsignmentMapper;
import com.manyun.business.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.comm.api.RemoteBuiUserService;
import com.manyun.comm.api.domain.dto.CntUserDto;
import com.manyun.common.core.constant.BusinessConstants;
import com.manyun.common.core.constant.SecurityConstants;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.exception.ServiceException;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.manyun.common.core.constant.BusinessConstants.SystemTypeConstant.CONSIGNMENT_DE_TIME;
import static com.manyun.common.core.enums.AliPayEnum.BOX_ALI_PAY;
import static com.manyun.common.core.enums.AliPayEnum.CONSIGNMENT_ALI_PAY;
import static com.manyun.common.core.enums.ConsignmentStatus.*;
import static com.manyun.common.core.enums.PayTypeEnum.MONEY_TAPE;
import static com.manyun.common.core.enums.WxPayEnum.BOX_WECHAT_PAY;
import static com.manyun.common.core.enums.WxPayEnum.CONSIGNMENT_WECHAT_PAY;

/**
 * <p>
 * 寄售市场主表 服务实现类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-30
 */
@Service
@Slf4j
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

    @Autowired
    private RootPay rootPay;

    @Autowired
    private IMsgService msgService;


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
//        if (Integer.valueOf(1).equals(consignmentQuery.getIsNew()))
//            lambdaQueryWrapper.orderByDesc(CntConsignment::getUpdatedTime);
        if (!Integer.valueOf(-1).equals(consignmentQuery.getPriceOrder())){
            if (Objects.nonNull(consignmentQuery.getPriceOrder()) && Integer.valueOf(0).equals(consignmentQuery.getPriceOrder()))
                lambdaQueryWrapper.orderByDesc(CntConsignment::getConsignmentPrice);
            if (Objects.nonNull(consignmentQuery.getPriceOrder()) && Integer.valueOf(1).equals(consignmentQuery.getPriceOrder()))
                lambdaQueryWrapper.orderByAsc(CntConsignment::getConsignmentPrice);
        }


        if (!Integer.valueOf(-1).equals(consignmentQuery.getTimeOrder())){
            if (Objects.nonNull(consignmentQuery.getTimeOrder()) && Integer.valueOf(0).equals(consignmentQuery.getTimeOrder()))
                lambdaQueryWrapper.orderByDesc(CntConsignment::getCreatedTime);
            if (Objects.nonNull(consignmentQuery.getTimeOrder()) && Integer.valueOf(1).equals(consignmentQuery.getTimeOrder()))
                lambdaQueryWrapper.orderByAsc(CntConsignment::getCreatedTime);
        }

        return lambdaQueryWrapper;
    }

    /**
     * 分页查询寄售市场盲盒的信息
     * @param consignmentQuery
     * @return
     */
    @Override
    public TableDataInfo<ConsignmentBoxListVo> pageConsignmentBoxList(ConsignmentQuery consignmentQuery) {
        PageHelper.startPage(consignmentQuery.getPageNum(),consignmentQuery.getPageSize());
        LambdaQueryWrapper<CntConsignment> lambdaQueryWrapper = getCntConsignmentLambdaQueryWrapper(consignmentQuery,BusinessConstants.ModelTypeConstant.BOX_TAYPE);
        List<CntConsignment> cntConsignments = list(lambdaQueryWrapper);
        // 组合数据
        return TableDataInfoUtil.pageTableDataInfo(encapsulationBoxListVo(cntConsignments),cntConsignments);
    }

    /**
     * 分页查询寄售订单列表
     * @param userId
     * @param consignmentOrderQuery
     * @return
     */
    @Override
    public TableDataInfo<ConsignmentOrderVo> consignmentPageOrder(String userId, ConsignmentOrderQuery consignmentOrderQuery) {
        PageHelper.startPage(consignmentOrderQuery.getPageNum(),consignmentOrderQuery.getPageSize());
        LambdaQueryWrapper<CntConsignment> queryWrapper = Wrappers.<CntConsignment>lambdaQuery().eq(CntConsignment::getSendUserId, userId).eq(Objects.nonNull(consignmentOrderQuery.getConsignmentStatus()), CntConsignment::getConsignmentStatus, consignmentOrderQuery.getConsignmentStatus()).orderByDesc(CntConsignment::getCreatedTime);
        List<CntConsignment> cntConsignments = list(queryWrapper);
        return TableDataInfoUtil.pageTableDataInfo(cntConsignments.parallelStream().map(this::initConsignmentOrderVo).collect(Collectors.toList()), cntConsignments);
    }

    /**
     *
     * @param payUserId  购买者用户编号
     * @param consignmentSellForm  寄售提交表单
     *
     * 对应寄售资产进行 购买操作!!!
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized PayVo businessConsignment(String payUserId, ConsignmentSellForm consignmentSellForm) {
        CntConsignment consignment = getById(consignmentSellForm.getBuiId());
        // 校验 购买条件是否符合！！！
        checkBusinessConsignment(payUserId,consignment);

        // 为买方新增订单！！！
        String hostOut = orderService.createConsignmentOrder(OrderCreateDto.builder()
                .orderAmount(consignment.getConsignmentPrice())
                .buiId(consignment.getRealBuiId())
                .payType(consignmentSellForm.getPayType())
                .goodsType(consignment.getIsType())
                .collectionName(consignment.getBuiName())
                .goodsNum(Integer.valueOf(1))
                .userId(payUserId).build(),(idStr)-> consignment.setOrderId(idStr));


        /**
         * 根据类型  发起支付订单
         * 余额支付直接扣除 订单状态做变更即可
         **/
        PayVo payVo =  rootPay.execPayVo(
                PayInfoDto.builder()
                        .payType(consignmentSellForm.getPayType())
                        .realPayMoney(consignment.getConsignmentPrice())
                        .outHost(hostOut)
                        .aliPayEnum(CONSIGNMENT_ALI_PAY)
                        .wxPayEnum(CONSIGNMENT_WECHAT_PAY)
                        .userId(payUserId).build());
        Order order = orderService.getOne(Wrappers.<Order>lambdaQuery().eq(Order::getOrderNo, hostOut));
        // 走这一步如果 是余额支付 那就说明扣款成功了！！！
        order.setMoneyBln(payVo.getMoneyBln());
        orderService.updateById(order);
        // 修改寄售信息
        consignment.updateD(payUserId);
        consignment.setPayUserId(payUserId);
        consignment.setFormInfo("资产已被锁单,等待买家付款.");
        consignment.setConsignmentStatus(LOCK_CONSIGN.getCode());
        updateById(consignment);

        // 如果说是 余额支付的,并且 将对应的状态都进行修复下
        if ( StrUtil.isBlank(payVo.getBody())){
            // 调用完成订单
            orderService.notifyPayConsignmentSuccess(payVo.getOutHost());
            String title = "";
            String form = "";
            if (BusinessConstants.ModelTypeConstant.BOX_TAYPE.equals(order.getGoodsType())){
                // 盲盒
                Box box = boxService.getById(order.getBuiId());
                title = StrUtil.format("购买了 {} 盲盒!", box.getBoxTitle());
                form = StrUtil.format("使用余额{};购买了 {} 盲盒!",order.getOrderAmount(), box.getBoxTitle());

            }
            if (BusinessConstants.ModelTypeConstant.COLLECTION_TAYPE.equals(order.getGoodsType())){
                //藏品
                CntCollection cntCollection = collectionService.getById(order.getBuiId());
                title = StrUtil.format("从寄售市场购买了 {} 藏品!", cntCollection.getCollectionName());
                form = StrUtil.format("使用余额{};从寄售市场购买了 {} 藏品!",order.getOrderAmount(), cntCollection.getCollectionName());
            }
            msgService.saveMsgThis(MsgThisDto.builder().userId(order.getUserId()).msgForm(form).msgTitle(title).build());
            msgService.saveCommMsg(MsgCommDto.builder().msgTitle(title).msgForm(form).build());
        }
        return payVo;
    }

    /**
     * 批量更改寄售状态  未支付成功,全部重置即可
     * @param cntConsignments
     */
    @Override
    public void reLoadConsignments(List<CntConsignment> cntConsignments) {
        String format = StrUtil.format("未到支付时间支付,已经取消！");
        for (CntConsignment cntConsignment : cntConsignments) {
            cntConsignment.setConsignmentStatus(PUSH_CONSIGN.getCode());
            cntConsignment.setFormInfo(format);
            cntConsignment.updateD(cntConsignment.getPayUserId());

        }
           updateBatchById(cntConsignments);
    }

    /**
     * 取消寄售市场中的资产
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public  synchronized void  cancelSchedulingConsignment() {
        // 小时为单位
        Integer time = systemService.getVal(CONSIGNMENT_DE_TIME, Integer.class);
        List<CntConsignment> cntConsignments = list(Wrappers.<CntConsignment>lambdaQuery().eq(CntConsignment::getConsignmentStatus,PUSH_CONSIGN.getCode()).lt(CntConsignment::getCreatedTime, LocalDateTime.now().minusHours(time)));
        // 开始对这些寄售资产进行取消操作
        for (CntConsignment cntConsignment : cntConsignments) {
            try {
                cancelConsignment(cntConsignment);
            }catch (Exception e){
                log.error("寄售市场撤回寄售资产错误信息:{},资产编号:{}",e.getMessage(),cntConsignment.getId());
            }

        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized void cancelConsignmentById(String id,String sendUserId) {
        CntConsignment consignment = getOne(Wrappers.<CntConsignment>lambdaQuery().eq(CntConsignment::getId, id).eq(CntConsignment::getSendUserId, sendUserId).eq(CntConsignment::getConsignmentStatus, PUSH_CONSIGN.getCode()));
        // 判定条件是否符合
        Assert.isTrue(Objects.nonNull(consignment),"取消寄售资产有误,请核实当前寄售状态!");
        cancelConsignment(consignment);
    }

    @Override
    public List<KeywordVo> queryDict(String keyword) {
        List<CntConsignment> cntConsignments = list(Wrappers.<CntConsignment>lambdaQuery().select(CntConsignment::getBuiName,CntConsignment::getIsType).like(CntConsignment::getBuiName, keyword).eq(CntConsignment::getConsignmentStatus, PUSH_CONSIGN.getCode()).orderByDesc(CntConsignment::getCreatedTime).last(" limit 10"));
       return cntConsignments.parallelStream().map(item ->{
            KeywordVo keywordVo = Builder.of(KeywordVo::new).build();
            keywordVo.setType(item.getIsType());
            keywordVo.setCommTitle(item.getBuiName());
            return keywordVo;
        }).collect(Collectors.toList());
    }

    private void cancelConsignment(CntConsignment cntConsignment) {
       // 1. 回滚到 用户资产中间表
        Integer isType = cntConsignment.getIsType();
        //验证是 藏品信息 还是 盲盒信息
        if (BusinessConstants.ModelTypeConstant.COLLECTION_TAYPE.equals(isType))
            //藏品
            userCollectionService.showUserCollection(cntConsignment.getSendUserId(),cntConsignment.getBuiId(),StrUtil.format("寄售的藏品,已被退回!"));
        if (BusinessConstants.ModelTypeConstant.BOX_TAYPE.equals(isType))
            // 盲盒
            userBoxService.showUserBox(cntConsignment.getBuiId(),cntConsignment.getSendUserId(),StrUtil.format("寄售的盲盒,已被退回!"));

        // 2. 删除当前寄售订单
        removeById(cntConsignment.getId());
       // 3. 记录日志

       // 4. 推送
    }

    /**
     * 校验是否 满足购买条件
     * @param payUserId
     * @param consignment
     */
    private void checkBusinessConsignment(String payUserId, CntConsignment consignment) {
        // 1.当前用户是否有对应未支付的订单
        //是否有未支付订单
        List<Order> orders = orderService.checkUnpaidOrder(payUserId);
        Assert.isFalse(orders.size() > 0 ,"您有未支付订单，暂不可购买");
        // 此寄售是否 状态是否正确！！！
        Assert.isTrue(PUSH_CONSIGN.getCode().equals(consignment.getConsignmentStatus()),"当前寄售资产已被锁单,请稍后重试!");
        Assert.isFalse(payUserId.equals(consignment.getSendUserId()),"自己不可购买自己寄售的产品!");

    }

    /**
     * 组装 寄售订单视图信息
     * @param cntConsignment
     * @return
     */
    private ConsignmentOrderVo initConsignmentOrderVo(CntConsignment cntConsignment) {
        ConsignmentOrderVo consignmentOrderVo = Builder.of(ConsignmentOrderVo::new).build();
        consignmentOrderVo.setCntUserDto(remoteBuiUserService.commUni(cntConsignment.getSendUserId(),  SecurityConstants.INNER).getData());
        consignmentOrderVo.setId(cntConsignment.getId());
        consignmentOrderVo.setConsignmentStatus(cntConsignment.getConsignmentStatus());
        consignmentOrderVo.setCreatedTime(cntConsignment.getCreatedTime());
        consignmentOrderVo.setType(cntConsignment.getIsType());
        consignmentOrderVo.setServerCharge(cntConsignment.getServerCharge());
        consignmentOrderVo.setConsignmentPrice(cntConsignment.getConsignmentPrice());
        consignmentOrderVo.setBuiId(cntConsignment.getBuiId());
        // 需要验证订单 才可以拿到此值
        if (LOCK_CONSIGN.getCode().equals(cntConsignment.getConsignmentStatus()) && StrUtil.isNotBlank(cntConsignment.getOrderId())){
            // 订单查询 将剩余支付时间补足即可
            Order order = orderService.getById(cntConsignment.getOrderId());
            consignmentOrderVo.setEndPayTime(order.getEndTime());
        }
        //验证是 藏品信息 还是 盲盒信息
        if (BusinessConstants.ModelTypeConstant.COLLECTION_TAYPE.equals(cntConsignment.getIsType()))
            //藏品
            consignmentOrderVo.setCollectionVo(collectionService.getBaseCollectionVo(cntConsignment.getRealBuiId()));

        if (BusinessConstants.ModelTypeConstant.BOX_TAYPE.equals(cntConsignment.getIsType()))
            // 盲盒
            consignmentOrderVo.setBoxListVo(boxService.getBaseBoxListVo(cntConsignment.getRealBuiId()));

        return consignmentOrderVo;
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
        consignmentBoxListVo.setConsignmentPrice(cntConsignment.getConsignmentPrice());
        consignmentBoxListVo.setServerCharge(cntConsignment.getServerCharge());
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
        // 脱敏
        CntUserDto cntUserDto = remoteBuiUserService.commUni(cntConsignment.getSendUserId(), SecurityConstants.INNER).getData();
        CntUserDto newCntUserDto = new CntUserDto();
        newCntUserDto.setId(cntUserDto.getId());
        newCntUserDto.setHeadImage(cntUserDto.getHeadImage());
        newCntUserDto.setNickName(cntUserDto.getNickName());
        newCntUserDto.setPhone(cntUserDto.getPhone());
        newCntUserDto.setLinkAddr(cntUserDto.getLinkAddr());

        collectionListVo.setCntUserDto(newCntUserDto);
        collectionListVo.setId(cntConsignment.getId());
        collectionListVo.setConsignmentStatus(cntConsignment.getConsignmentStatus());
        collectionListVo.setCreatedTime(cntConsignment.getCreatedTime());
        collectionListVo.setConsignmentPrice(cntConsignment.getConsignmentPrice());
        collectionListVo.setServerCharge(cntConsignment.getServerCharge());

        UserCollection userCollection = userCollectionService.getById(cntConsignment.getBuiId());
        collectionListVo.setCollectionNumber(userCollection.getCollectionNumber());
        collectionListVo.setBuiId(userCollection.getId());
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
           // return;
        }
       // 盲盒
        if (BusinessConstants.ModelTypeConstant.BOX_TAYPE.equals(type)){
            // 将自己的盲盒 隐藏,归并状态及词条
             info = StrUtil.format("该盲盒被寄售了,已将盲盒移送到寄售市场!");
            realBuiId = userBoxService.hideUserBox(buiId,userId,info);
            Box box = boxService.getById(realBuiId);
            cateId = box.getCateId();
            buiName= box.getBoxTitle();
           // return;
        }
        if (StrUtil.isBlank(info) && StrUtil.isBlank(realBuiId))
           throw new ServiceException("not fount type [0-1] now type is "+type+"");

        pushConsignment(userId,userConsignmentForm.getConsignmentMoney(), type, buiId, realBuiId, info,cateId,buiName);

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
