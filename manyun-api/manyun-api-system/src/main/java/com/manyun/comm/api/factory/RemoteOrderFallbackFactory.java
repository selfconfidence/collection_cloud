package com.manyun.comm.api.factory;

import com.manyun.comm.api.RemoteOrderService;
import com.manyun.comm.api.RemoteUserService;
import com.manyun.comm.api.domain.SysUser;
import com.manyun.comm.api.model.LoginUser;
import com.manyun.common.core.domain.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 业务订单服务降级处理
 * 
 * @author yanwei
 */
@Component
public class RemoteOrderFallbackFactory implements FallbackFactory<RemoteOrderService>
{
    private static final Logger log = LoggerFactory.getLogger(RemoteOrderFallbackFactory.class);

    @Override
    public RemoteOrderService create(Throwable throwable)
    {
        log.error("用户服务调用失败:{}", throwable.getMessage());
        return new RemoteOrderService()
        {

            @Override
            public R timeCancel(String source) {
                return R.fail("获取用户失败:" + throwable.getMessage());
            }

            @Override
            public R cancelOpenOrder(String orderId, String source) {
                return R.fail("获取用户失败:" + throwable.getMessage());
            }
        };
    }
}
