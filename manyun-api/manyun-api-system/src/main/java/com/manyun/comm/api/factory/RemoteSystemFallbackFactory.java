package com.manyun.comm.api.factory;

import com.manyun.comm.api.RemoteBoxService;
import com.manyun.comm.api.RemoteSystemService;
import com.manyun.comm.api.domain.dto.OpenPleaseBoxDto;
import com.manyun.common.core.domain.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 业务系统配置参数服务降级处理
 * 
 * @author ruoyi
 */
@Component
public class RemoteSystemFallbackFactory implements FallbackFactory<RemoteSystemService>
{
    private static final Logger log = LoggerFactory.getLogger(RemoteSystemFallbackFactory.class);

    @Override
    public RemoteSystemService create(Throwable throwable)
    {
        log.error("业务系统参数配置服务调用失败:{}", throwable.getMessage());
        return new RemoteSystemService() {
            @Override
            public R<String> findType(String type, String source) {
                return R.fail("操作失败:" + throwable.getMessage());
            }
        };
    }
}
