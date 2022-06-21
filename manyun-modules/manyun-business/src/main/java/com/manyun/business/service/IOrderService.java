package com.manyun.business.service;

import com.manyun.business.domain.dto.OrderCreateDto;
import com.manyun.business.domain.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

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

    /**
     * 创建订单
     * @param orderCreateDto
     * @return
     */
    String createOrder(OrderCreateDto orderCreateDto);

    void notifyPaySuccess(String outHost);
}
