package com.manyun.comm.api;

import com.manyun.comm.api.domain.dto.CntUserDto;
import com.manyun.comm.api.factory.RemoteBuiMoneyFallbackFactory;
import com.manyun.comm.api.factory.RemoteBuiUserFallbackFactory;
import com.manyun.comm.api.model.LoginPhoneCodeForm;
import com.manyun.comm.api.model.LoginPhoneForm;
import com.manyun.common.core.constant.SecurityConstants;
import com.manyun.common.core.constant.ServiceNameConstants;
import com.manyun.common.core.domain.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 业务板块 用户服务
 */
@FeignClient(contextId = "remoteBuiMoneyService", value = ServiceNameConstants.BUSINESS_SERVICE, fallbackFactory = RemoteBuiMoneyFallbackFactory.class)
public interface RemoteBuiMoneyService {

    @GetMapping("/money/initUserMoney/{userId}")
     R initUserMoney(@PathVariable String userId,@RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
