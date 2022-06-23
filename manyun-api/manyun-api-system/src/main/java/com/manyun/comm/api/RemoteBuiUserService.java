package com.manyun.comm.api;

import com.manyun.comm.api.domain.CntUser;
import com.manyun.comm.api.factory.RemoteBuiUserFallbackFactory;
import com.manyun.comm.api.factory.RemoteUserFallbackFactory;
import com.manyun.comm.api.model.LoginPhoneCodeForm;
import com.manyun.comm.api.model.LoginPhoneForm;
import com.manyun.common.core.constant.ServiceNameConstants;
import com.manyun.common.core.domain.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 业务板块 用户服务
 */
@FeignClient(contextId = "remoteBuiUserService", value = ServiceNameConstants.BUI_USER_SERVICE, fallbackFactory = RemoteBuiUserFallbackFactory.class)
public interface RemoteBuiUserService {

    /**
     * 用户登录
     */
    @PostMapping("/cntUser/login")
     R<CntUser> login(@RequestBody LoginPhoneForm loginPhoneForm);

    @PostMapping("/codeLogin")
     R<CntUser> codeLogin(@RequestBody LoginPhoneCodeForm loginPhoneCodeForm);

}
