package com.manyun.comm.api.factory;

import com.manyun.comm.api.RemoteFileService;
import com.manyun.comm.api.RemoteSmsService;
import com.manyun.comm.api.domain.dto.SmsCommDto;
import com.manyun.common.core.domain.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * 短信服务降级处理
 * 
 * @author yanwei
 */
@Component
public class RemoteSmsFallbackFactory implements FallbackFactory<RemoteSmsService>
{
    private static final Logger log = LoggerFactory.getLogger(RemoteSmsFallbackFactory.class);

    @Override
    public RemoteSmsService create(Throwable throwable)
    {
        log.error("发送短信失败:{}", throwable.getMessage());
        return new RemoteSmsService()
        {

            @Override
            public R sendCommPhone(SmsCommDto smsCommDto) {
                return R.fail("发送短信失败:" + throwable.getMessage());
            }
        };
    }
}
