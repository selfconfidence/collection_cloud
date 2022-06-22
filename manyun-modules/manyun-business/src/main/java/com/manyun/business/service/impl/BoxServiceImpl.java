package com.manyun.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.manyun.business.design.pay.RootPay;
import com.manyun.business.domain.dto.OrderCreateDto;
import com.manyun.business.domain.dto.PayInfoDto;
import com.manyun.business.domain.entity.Box;
import com.manyun.business.domain.entity.Media;
import com.manyun.business.domain.entity.Money;
import com.manyun.business.domain.entity.Order;
import com.manyun.business.domain.form.BoxSellForm;
import com.manyun.business.domain.query.BoxQuery;
import com.manyun.business.domain.vo.*;
import com.manyun.business.mapper.BoxMapper;
import com.manyun.business.mapper.MediaMapper;
import com.manyun.business.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.common.core.constant.BusinessConstants;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.enums.BoxStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.manyun.common.core.enums.AliPayEnum.BOX_ALI_PAY;
import static com.manyun.common.core.enums.BoxStatus.DOWN_ACTION;
import static com.manyun.common.core.enums.PayTypeEnum.MONEY_TAPE;
import static com.manyun.common.core.enums.WxPayEnum.BOX_WECHAT_PAY;

/**
 * <p>
 * 盲盒;盲盒主体表 服务实现类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@Service
public class BoxServiceImpl extends ServiceImpl<BoxMapper, Box> implements IBoxService {

    @Autowired
    private IMediaService mediaService;

    @Autowired
    private IBoxCollectionService  boxCollectionService;

    @Resource
    private BoxMapper boxMapper;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IMoneyService moneyService;

    @Autowired
    private RootPay rootPay;



    /**
     * 分页查询盲盒列表
     * @param boxQuery
     * @return
     */
    @Override
    public List<BoxListVo> pageList(BoxQuery boxQuery) {
        PageHelper.startPage(boxQuery.getPageNum(),boxQuery.getPageSize());
        List<Box> boxList = list(Wrappers.<Box>lambdaQuery().eq(StrUtil.isNotBlank(boxQuery.getCateId()),Box::getCateId,boxQuery.getCateId()).like(StrUtil.isNotBlank(boxQuery.getBoxName()), Box::getBoxTitle, boxQuery.getBoxName()).ne(Box::getStatusBy,DOWN_ACTION.getCode()).orderByDesc(Box::getCreatedTime));
        // 数据组合查询
        return boxList.parallelStream().map(this::initBoxListVo).collect(Collectors.toList());
    }

    /**
     * 根据盲盒编号 查询详细信息
     * @param id
     * @return
     */
    @Override
    public BoxVo info(String id) {
        Box box = getById(id);
        BoxListVo boxListVo = initBoxListVo(box);
        BoxVo boxVo = Builder.of(BoxVo::new).build();
        boxVo.setBoxListVo(boxListVo);
        // 组合与藏品关联数据
        boxVo.setBoxCollectionJoinVos(boxCollectionService.findJoinCollections(id));
        return boxVo;
    }

    /**
     * 购买盲盒
     * @param boxSellForm
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PayVo sellBox(BoxSellForm boxSellForm,String userId) {
        //是否有未支付订单
        List<Order> orders = orderService.checkUnpaidOrder(userId);
        Assert.isFalse(orders.size() > 0 ,"您有未支付订单，暂不可购买");
        // 总结校验 —— 支付方式
        Box box = getById(boxSellForm.getBoxId());
        // 实际需要支付的金额
        BigDecimal realPayMoney = NumberUtil.mul(boxSellForm.getSellNum(), box.getRealPrice());
        checkSell(box,userId,boxSellForm.getPayType(),realPayMoney);
        // 锁优化
        int rows = boxMapper.updateLock(box.getId(),boxSellForm.getSellNum());
         Assert.isTrue(rows>=1,"您来晚了!");

        // 根据支付方式创建订单  通用适配方案 余额直接 减扣操作
        //1. 先创建对应的订单
       String outHost =   orderService.createOrder(OrderCreateDto.builder()
               .orderAmount(realPayMoney)
               .buiId(box.getId())
               .payType(boxSellForm.getPayType())
               .goodsType(BusinessConstants.ModelTypeConstant.BOX_TAYPE)
               .collectionName(box.getBoxTitle())
               .goodsNum(boxSellForm.getSellNum())
               .userId(userId)
               .build());
        //2. 然后调取对应支付即可
        /**
         * 根据类型  发起支付订单
         * 余额支付直接扣除 订单状态做变更即可
         **/
        PayVo payVo =  rootPay.execPayVo(
                PayInfoDto.builder()
                        .payType(boxSellForm.getPayType())
                        .realPayMoney(realPayMoney)
                        .outHost(outHost)
                        .aliPayEnum(BOX_ALI_PAY)
                        .wxPayEnum(BOX_WECHAT_PAY)
                        .userId(userId).build());
        return payVo;



    }



    private void checkSell(Box box,String userId,Integer payType,BigDecimal realPayMoney) {
        // 校验盲盒主体是否存在
        Assert.isTrue(Objects.nonNull(box),"盲盒编号有误,请核实!");
        // 校验盲盒是否到了发行时间
        Assert.isTrue(LocalDateTime.now().compareTo(box.getPublishTime()) >= 0,"发行时间未到,请核实!");
        // 校验是否库存不够了
        Assert.isFalse(Integer.valueOf(0).equals(box.getBalance()),"库存不足了!");
        // 重复校验状态
        Assert.isTrue(BoxStatus.UP_ACTION.getCode().equals(box.getStatusBy()),"您来晚了,售罄了!");
       // 如果是余额支付,需要验证下是否充足
        if (MONEY_TAPE.getCode().equals(payType)) {
            Money money = moneyService.getOne(Wrappers.<Money>lambdaQuery().eq(Money::getUserId, userId));
           Assert.isTrue(money.getMoneyBalance().compareTo(realPayMoney) >=0,"余额不足,请核实!");
        }

    }


    /**
     * 转义数据
     * @param box
     * @return
     */
    private BoxListVo initBoxListVo(Box box){
        BoxListVo boxListVo = Builder.of(BoxListVo::new).build();
        BeanUtil.copyProperties(box,boxListVo);
        // 需要集成图片服务
        boxListVo.setMediaVos(mediaService.initMediaVos(box.getId(), BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE));
        return boxListVo;

    }


}
