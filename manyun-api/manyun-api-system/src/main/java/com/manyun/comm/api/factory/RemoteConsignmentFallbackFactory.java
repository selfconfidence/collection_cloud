package com.manyun.comm.api.factory;

import com.manyun.comm.api.RemoteBoxService;
import com.manyun.comm.api.RemoteConsignmentService;
import com.manyun.comm.api.domain.dto.OpenPleaseBoxDto;
import com.manyun.common.core.domain.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 业务寄售服务降级处理
 * 
 * @author yanwei
 */
@Component
public class RemoteConsignmentFallbackFactory implements FallbackFactory<RemoteConsignmentService>
{
    private static final Logger log = LoggerFactory.getLogger(RemoteConsignmentFallbackFactory.class);

    @Override
    public RemoteConsignmentService create(Throwable throwable)
    {
        log.error("业务寄售服务调用失败:{}", throwable.getMessage());
        return new RemoteConsignmentService() {

            @Override
            public R cancelSchedulingConsignment(String source) {
                return R.fail("出错了!!!");
            }

            @Override
            public R consignmentSuccess(String id, String source) {
                return R.fail("出错了!!!");
            }
        };
    }
}
