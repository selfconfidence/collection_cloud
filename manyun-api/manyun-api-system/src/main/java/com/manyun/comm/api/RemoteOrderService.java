package com.manyun.comm.api;

import com.manyun.comm.api.domain.SysUser;
import com.manyun.comm.api.factory.RemoteOrderFallbackFactory;
import com.manyun.comm.api.factory.RemoteUserFallbackFactory;
import com.manyun.comm.api.model.LoginUser;
import com.manyun.common.core.constant.SecurityConstants;
import com.manyun.common.core.constant.ServiceNameConstants;
import com.manyun.common.core.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 订单服务
 * 
 * @author ruoyi
 */
@FeignClient(contextId = "remoteOrderService", value = ServiceNameConstants.BUSINESS_SERVICE, fallbackFactory = RemoteOrderFallbackFactory.class)
public interface RemoteOrderService
{
    /**
     * 定时调度取消未支付的订单
     *
     * @return 结果
     */
    @GetMapping("/order/timeCancel")
     R timeCancel( @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 取消订单
     */
    @GetMapping("/order/cancelOpenOrder/{orderId}")
    R cancelOpenOrder(@PathVariable("orderId") String orderId,@RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
