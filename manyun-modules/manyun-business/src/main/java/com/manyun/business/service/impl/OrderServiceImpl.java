package com.manyun.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manyun.business.domain.dto.OrderCreateDto;
import com.manyun.business.domain.entity.Order;
import com.manyun.business.domain.query.OrderQuery;
import com.manyun.business.domain.vo.OrderVo;
import com.manyun.business.mapper.OrderMapper;
import com.manyun.business.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.common.core.domain.Builder;
import com.manyun.business.service.ISystemService;
import com.manyun.business.service.IUserBoxService;
import com.manyun.business.service.IUserCollectionService;
import com.manyun.common.core.constant.BusinessConstants;
import com.manyun.common.core.domain.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.manyun.common.core.enums.OrderStatus.OVER_ORDER;
import static com.manyun.common.core.enums.OrderStatus.WAIT_ORDER;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Override
    public List<OrderVo> pageQueryList(OrderQuery orderQuery) {
        List<Order> orderList = list(Wrappers.<Order>lambdaQuery()
                .eq(orderQuery.getOrderStatus() != null, Order::getOrderStatus, orderQuery.getOrderStatus())
                .orderByDesc(Order::getCreatedTime));
        return orderList.parallelStream().map(this::providerOrderVo).collect(Collectors.toList());
    }

    private OrderVo providerOrderVo(Order order) {
        OrderVo orderVo = Builder.of(OrderVo::new).build();
        BeanUtil.copyProperties(order, orderVo);
        return orderVo;
    }


    @Autowired
    private ISystemService systemService;

    @Autowired
    private IUserBoxService userBoxService;

    @Autowired
    private IUserCollectionService userCollectionService;


    /**
     * 创建订单 ,进行订单初始化
     * @param orderCreateDto  实际支付金额
     * @return
     */
    @Override
    public String createOrder(OrderCreateDto orderCreateDto) {
        Order order = Builder.of(Order::new).build();
        BeanUtil.copyProperties(orderCreateDto,order);
        order.createD(orderCreateDto.getUserId());
        // 生成支付编号
        String idStr = IdUtil.getSnowflake().nextIdStr();
        order.setId(idStr);
        String orderNo = IdUtil.objectId();
        order.setOrderNo(orderNo);
        // 待支付状态
        order.setOrderStatus(WAIT_ORDER.getCode());
        order.setPayTime(LocalDateTime.now());
        // 截止到什么是否到达付款时间 小时为单位
        Integer serviceVal = systemService.getVal(BusinessConstants.SystemTypeConstant.ORDER_END_TIME, Integer.class);
        order.setEndTime(LocalDateTime.now().plusHours(serviceVal));
        save(order);
        return orderNo;
    }


    /**
     * 完成订单操作
     * @param outHost
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized void notifyPaySuccess(String outHost) {
        Order order = getOne(Wrappers.<Order>lambdaQuery().eq(Order::getOrderNo, outHost));
        String info = StrUtil.format("从平台购买,本次消费{},来源为平台发售", order.getOrderAmount().toString());
        Assert.isTrue(Objects.nonNull(order),"找不到对应订单编号!");
        Assert.isTrue(WAIT_ORDER.getCode().equals(order.getOrderStatus()),"订单状态有误,请核实!");
        // 更改订单状态, 绑定对应的 （藏品/盲盒）
        order.setOrderStatus(OVER_ORDER.getCode());
        order.updateD(order.getUserId());
        updateById(order);
        // 开始绑定 根据购买的藏品/盲盒进行绑定
        Integer goodsType = order.getGoodsType();
        if (BusinessConstants.ModelTypeConstant.BOX_TAYPE.equals(goodsType)) {
            // 盲盒
            userBoxService.bindBox(order.getUserId(),order.getBuiId(),info,order.getGoodsNum());
            return;
        }

        if (BusinessConstants.ModelTypeConstant.COLLECTION_TAYPE.equals(goodsType)) {
           // 藏品
            userCollectionService.bindCollection(order.getUserId(),order.getBuiId(),info,order.getGoodsNum());
            return;
        }
        throw new IllegalStateException("not fount order good_type = " + goodsType);

    }

}
