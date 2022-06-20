package com.manyun.auth.controller;

import com.manyun.auth.form.LoginBody;
import com.manyun.comm.api.domain.CntUser;
import com.manyun.comm.api.model.LoginBusinessUser;
import com.manyun.comm.api.model.LoginUser;
import com.manyun.common.core.domain.R;
import com.manyun.common.security.service.TokenService;
import com.manyun.common.security.service.UserTokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/login")
    @ApiOperation("移动端手机验证码登录")
    public R<?> login(@RequestBody  LoginBody form)
    {
        // 用户登录
        // 获取登录token
        LoginBusinessUser loginBusinessUser = new LoginBusinessUser();
        CntUser cntUser = new CntUser();
        cntUser.setId("123");
        cntUser.setNickName("zhangsan");
        loginBusinessUser.setCntUser(cntUser);
        return R.ok(userTokenService.createToken(loginBusinessUser));
    }

}
