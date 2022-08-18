package com.manyun.comm.api;

import com.manyun.comm.api.domain.form.UserRealMoneyForm;
import com.manyun.comm.api.factory.RemoteBuiMoneyFallbackFactory;
import com.manyun.common.core.constant.SecurityConstants;
import com.manyun.common.core.constant.ServiceNameConstants;
import com.manyun.common.core.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 业务板块 用户服务
 */
@FeignClient(contextId = "remoteBuiMoneyService", value = ServiceNameConstants.BUSINESS_SERVICE, fallbackFactory = RemoteBuiMoneyFallbackFactory.class)
public interface RemoteBuiMoneyService {

    @GetMapping("/money/initUserMoney/{userId}")
     R initUserMoney(@PathVariable("userId") String userId,@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    @PostMapping("/money/updateUserMoney")
     R updateUserMoney(@RequestBody UserRealMoneyForm userRealMoneyForm, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    @GetMapping("/money/checkIdentity/{identityNo}")
    R checkIdentity(@PathVariable("identityNo") String identityNo, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}
