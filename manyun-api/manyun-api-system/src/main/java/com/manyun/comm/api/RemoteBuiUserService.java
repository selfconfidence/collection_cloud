package com.manyun.comm.api;

import com.manyun.comm.api.domain.dto.CntUserDto;
import com.manyun.comm.api.domain.dto.UserDto;
import com.manyun.comm.api.domain.form.JgLoginTokenForm;
import com.manyun.comm.api.factory.RemoteBuiUserFallbackFactory;
import com.manyun.comm.api.model.LoginPhoneCodeForm;
import com.manyun.comm.api.model.LoginPhoneForm;
import com.manyun.common.core.constant.SecurityConstants;
import com.manyun.common.core.constant.ServiceNameConstants;
import com.manyun.common.core.domain.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 业务板块 用户服务
 */
@FeignClient(contextId = "remoteBuiUserService", value = ServiceNameConstants.BUI_USER_SERVICE, fallbackFactory = RemoteBuiUserFallbackFactory.class)
public interface RemoteBuiUserService {

    /**
     * 用户登录
     */
     @PostMapping("/cntUser/login")
     R<CntUserDto> login(@RequestBody LoginPhoneForm loginPhoneForm,@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

     @PostMapping("/cntUser/codeLogin")
     R<CntUserDto> codeLogin(@RequestBody LoginPhoneCodeForm loginPhoneCodeForm,@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    @GetMapping("/cntUser/commUni/{commUni}")
     R<CntUserDto> commUni(@PathVariable(name = "commUni") String commUni, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
    @PostMapping("/cntUser/jgPhoneLogin")
     R<CntUserDto> jgPhoneLogin(@RequestBody JgLoginTokenForm jgLoginTokenForm, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    @PostMapping("/cntUser/findUserIdLists")
    R<List<CntUserDto>> findUserIdLists(@RequestBody UserDto userDto, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);


}
