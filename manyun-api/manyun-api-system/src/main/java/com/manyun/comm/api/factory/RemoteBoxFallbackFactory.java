package com.manyun.comm.api.factory;

import com.manyun.comm.api.RemoteBoxService;
import com.manyun.comm.api.domain.dto.OpenPleaseBoxDto;
import com.manyun.common.core.domain.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 文件服务降级处理
 * 
 * @author ruoyi
 */
@Component
public class RemoteBoxFallbackFactory implements FallbackFactory<RemoteBoxService>
{
    private static final Logger log = LoggerFactory.getLogger(RemoteBoxFallbackFactory.class);

    @Override
    public RemoteBoxService create(Throwable throwable)
    {
        log.error("业务盲盒服务调用失败:{}", throwable.getMessage());
        return new RemoteBoxService() {
            @Override
            public R openPleaseBox(OpenPleaseBoxDto pleaseBoxDto) {
                return R.fail("操作失败:" + throwable.getMessage());
            }
        };
    }
}
