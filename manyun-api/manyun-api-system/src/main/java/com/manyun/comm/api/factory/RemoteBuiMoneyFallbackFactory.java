package com.manyun.comm.api.factory;

import com.manyun.comm.api.RemoteBuiMoneyService;
import com.manyun.comm.api.domain.form.UserRealMoneyForm;
import com.manyun.common.core.domain.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 业务钱包服务降级处理
 * 
 * @author yanwei
 */
@Component
public class RemoteBuiMoneyFallbackFactory implements FallbackFactory<RemoteBuiMoneyService>
{
    private static final Logger log = LoggerFactory.getLogger(RemoteBuiMoneyFallbackFactory.class);

    @Override
    public RemoteBuiMoneyService create(Throwable throwable)
    {
        log.error("业务用户服务调用失败:{}", throwable.getMessage());
        return new RemoteBuiMoneyService() {
            @Override
            public R initUserMoney(String userId,String source) {
                return R.fail("操作失败:" + throwable.getMessage());
            }

            @Override
            public R updateUserMoney(UserRealMoneyForm userRealMoneyForm, String source) {
                return R.fail("操作失败:" + throwable.getMessage());
            }

            @Override
            public R checkIdentity(String identityNo, String source) {
                return R.fail("操作失败:" + throwable.getMessage());
            }
        };
    }
}
