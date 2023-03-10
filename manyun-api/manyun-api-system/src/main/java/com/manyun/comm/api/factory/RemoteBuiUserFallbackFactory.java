package com.manyun.comm.api.factory;

import com.manyun.comm.api.RemoteBuiUserService;
import com.manyun.comm.api.domain.dto.CntUserDto;
import com.manyun.comm.api.domain.dto.UserDto;
import com.manyun.comm.api.domain.form.JgLoginTokenForm;
import com.manyun.comm.api.model.LoginPhoneCodeForm;
import com.manyun.comm.api.model.LoginPhoneForm;
import com.manyun.common.core.domain.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 业务用户服务降级处理
 * 
 * @author yanwei
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

            @Override
            public R<CntUserDto> jgPhoneLogin(JgLoginTokenForm jgLoginTokenForm, String source) {
                return R.fail("一键登录失败:" + throwable.getMessage());
            }

            @Override
            public R<List<CntUserDto>> findUserIdLists(UserDto userDto, String source) {
                return R.fail("查询失败:" + throwable.getMessage());
            }
        };
    }
}
