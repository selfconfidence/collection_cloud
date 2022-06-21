package com.manyun.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manyun.business.domain.entity.Order;
import com.manyun.business.domain.query.OrderQuery;
import com.manyun.business.domain.vo.OrderVo;
import com.manyun.business.mapper.OrderMapper;
import com.manyun.business.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.common.core.domain.Builder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

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

}
