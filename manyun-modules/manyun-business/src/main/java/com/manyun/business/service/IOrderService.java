package com.manyun.business.service;

import com.manyun.business.domain.dto.OrderCreateDto;
import com.manyun.business.domain.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.business.domain.form.OrderPayForm;
import com.manyun.business.domain.query.OrderQuery;
import com.manyun.business.domain.vo.OrderInfoVo;
import com.manyun.business.domain.vo.OrderVo;
import com.manyun.business.domain.vo.PayVo;
import com.manyun.common.core.web.page.TableDataInfo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.math.BigDecimal;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
public interface IOrderService extends IService<Order> {
    TableDataInfo<OrderVo> pageQueryList(OrderQuery orderQuery, String userId) ;

    /**
     * 检查未支付订单
     * @param userId
     * @return
     */
    List<Order> checkUnpaidOrder (String userId);

    /**
     * 创建订单
     * @param orderCreateDto
     * @return
     */
    String createOrder(OrderCreateDto orderCreateDto);

    String createConsignmentOrder(OrderCreateDto orderCreateDto, Consumer<String> consumer);

    void notifyPaySuccess(String outHost);

    void timeCancel();

    void notifyPayConsignmentSuccess(String outHost);

    OrderInfoVo info(String id);

    PayVo unifiedOrder(OrderPayForm orderPayForm,String userId);

    void cancelOrder(String id);

//    PayVo consignmentUnifiedOrder(OrderPayForm orderPayForm, String userId);
}
