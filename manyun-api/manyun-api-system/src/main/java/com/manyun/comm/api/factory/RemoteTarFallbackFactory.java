package com.manyun.comm.api.factory;

import com.manyun.comm.api.RemoteBoxService;
import com.manyun.comm.api.RemoteTarService;
import com.manyun.comm.api.domain.dto.BoxListDto;
import com.manyun.comm.api.domain.dto.OpenPleaseBoxDto;
import com.manyun.common.core.domain.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 业务盲盒服务降级处理
 * 
 * @author yanwei
 */
@Component
public class RemoteTarFallbackFactory implements FallbackFactory<RemoteTarService>
{
    private static final Logger log = LoggerFactory.getLogger(RemoteTarFallbackFactory.class);

    @Override
    public RemoteTarService create(Throwable cause) {
        return new RemoteTarService() {
            @Override
            public R taskEndFlag(String source) {
                return R.fail(cause.getMessage());
            }
        };
    }
}
