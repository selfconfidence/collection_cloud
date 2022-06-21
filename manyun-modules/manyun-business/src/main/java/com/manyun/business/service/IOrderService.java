package com.manyun.business.service;

import com.manyun.business.domain.dto.OrderCreateDto;
import com.manyun.business.domain.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.business.domain.query.OrderQuery;
import com.manyun.business.domain.vo.OrderVo;

import java.util.List;

import java.math.BigDecimal;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
public interface IOrderService extends IService<Order> {
    List<OrderVo> pageQueryList(OrderQuery orderQuery) ;

    /**
     * 创建订单
     * @param orderCreateDto
     * @return
     */
    String createOrder(OrderCreateDto orderCreateDto);

    void notifyPaySuccess(String outHost);
}
