package com.manyun.comm.api.factory;

import com.manyun.comm.api.RemoteBuiUserService;
import com.manyun.comm.api.domain.dto.CntUserDto;
import com.manyun.comm.api.model.LoginPhoneCodeForm;
import com.manyun.comm.api.model.LoginPhoneForm;
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
public class RemoteBuiUserFallbackFactory implements FallbackFactory<RemoteBuiUserService>
{
    private static final Logger log = LoggerFactory.getLogger(RemoteBuiUserFallbackFactory.class);

    @Override
    public RemoteBuiUserService create(Throwable throwable)
    {
        log.error("业务用户服务调用失败:{}", throwable.getMessage());
        return new RemoteBuiUserService() {
            @Override
            public R<CntUserDto> login(LoginPhoneForm loginPhoneForm, String source) {
                return R.fail("登录失败:" + throwable.getMessage());
            }

            @Override
            public R<CntUserDto> codeLogin(LoginPhoneCodeForm loginPhoneCodeForm, String source) {
                return R.fail("登录失败:" + throwable.getMessage());
            }

            @Override
            public R<CntUserDto> commUni(String commUni, String source) {
                return R.fail("查询失败:" + throwable.getMessage());
            }
        };
    }
}
