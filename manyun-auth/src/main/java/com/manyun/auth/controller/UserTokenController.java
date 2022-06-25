package com.manyun.auth.controller;

import cn.hutool.core.lang.Assert;
import com.manyun.comm.api.RemoteBuiUserService;
import com.manyun.comm.api.domain.CntUser;
import com.manyun.comm.api.domain.vo.AccTokenVo;
import com.manyun.comm.api.model.LoginPhoneCodeForm;
import com.manyun.comm.api.model.LoginPhoneForm;
import com.manyun.common.core.constant.SecurityConstants;
import com.manyun.common.core.domain.CodeStatus;
import com.manyun.common.core.domain.R;
import com.manyun.common.redis.service.RedisService;
import com.manyun.common.security.service.UserTokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * token 控制
 *
 * @author yanwei
 */
@RestController
@Api(tags = "移动端用户登录注册相关")
@RequestMapping("/user")
public class UserTokenController {



    @Autowired
    private UserTokenService userTokenService;

    @Autowired
    private RemoteBuiUserService remoteBuiUserService;

    @PostMapping("/login")
    @ApiOperation(value = "用户登录",notes = "用户账号密码登录")
    public R<AccTokenVo> login(@RequestBody @Valid LoginPhoneForm loginPhoneForm)
    {
        R<CntUser> userR = remoteBuiUserService.login(loginPhoneForm);
        Assert.isTrue(userR.getCode() == CodeStatus.SUCCESS.getCode(),userR.getMsg());
        CntUser userRData = userR.getData();
        // 用户登录
        return R.ok(userTokenService.createToken(userRData));
    }


    @PostMapping("/codeLogin")
    @ApiOperation(value = "用户验证码登录",notes = "验证码登录")
    public R<AccTokenVo> codeLogin(@RequestBody LoginPhoneCodeForm loginPhoneCodeForm){
        R<CntUser> cntUserR = remoteBuiUserService.codeLogin(loginPhoneCodeForm);
        Assert.isTrue(cntUserR.getCode() == CodeStatus.SUCCESS.getCode(),cntUserR.getMsg());
        CntUser userRData = cntUserR.getData();
        return R.ok(userTokenService.createToken(userRData));
    }

}
