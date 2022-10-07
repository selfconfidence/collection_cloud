package com.manyun.auth.controller;

import cn.hutool.core.lang.Assert;
import com.dingxianginc.ctu.client.CaptchaClient;
import com.dingxianginc.ctu.client.model.CaptchaResponse;
import com.manyun.comm.api.RemoteBuiUserService;
import com.manyun.comm.api.domain.dto.CntUserDto;
import com.manyun.comm.api.domain.form.JgLoginTokenForm;
import com.manyun.comm.api.domain.vo.AccTokenVo;
import com.manyun.comm.api.model.LoginBusinessUser;
import com.manyun.comm.api.model.LoginPhoneCodeForm;
import com.manyun.comm.api.model.LoginPhoneForm;
import com.manyun.common.core.annotation.RequestBodyRsa;
import com.manyun.common.core.constant.SecurityConstants;
import com.manyun.common.core.domain.CodeStatus;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.utils.XXutils;
import com.manyun.common.security.service.UserTokenService;
import com.manyun.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public R<AccTokenVo> login(@RequestBodyRsa @Valid LoginPhoneForm loginPhoneForm)
    {
        XXutils.dingxiangTokenCheck(loginPhoneForm.getToken());
        R<CntUserDto> userR = remoteBuiUserService.login(loginPhoneForm, SecurityConstants.INNER);
        Assert.isTrue(userR.getCode() == CodeStatus.SUCCESS.getCode(),userR.getMsg());
        CntUserDto userRData = userR.getData();
        // 用户登录
        return R.ok(userTokenService.createToken(userRData));
    }
    @PostMapping("/loginRSA")
    @ApiOperation(value = "用户登录RSA",notes = "用户账号密码登录RSA",httpMethod = "POST",produces = "application/json;utf-8",consumes = "application/json;utf-8")
    public R<AccTokenVo> loginRSA(@RequestBodyRsa @Valid LoginPhoneForm loginPhoneForm)
    {
        R<CntUserDto> userR = remoteBuiUserService.login(loginPhoneForm, SecurityConstants.INNER);
        Assert.isTrue(userR.getCode() == CodeStatus.SUCCESS.getCode(),userR.getMsg());
        CntUserDto userRData = userR.getData();
        // 用户登录
        return R.ok(userTokenService.createToken(userRData));
    }

    @PostMapping("/codeLogin")
    @ApiOperation(value = "用户验证码登录",notes = "验证码登录")
    public R<AccTokenVo> codeLogin(@RequestBodyRsa @Valid LoginPhoneCodeForm loginPhoneCodeForm){
/*
        dingxiangTokenCheck(loginPhoneCodeForm.getToken());
*/
        R<CntUserDto> cntUserR = remoteBuiUserService.codeLogin(loginPhoneCodeForm,SecurityConstants.INNER);
        Assert.isTrue(cntUserR.getCode() == CodeStatus.SUCCESS.getCode(),cntUserR.getMsg());
        CntUserDto userRData = cntUserR.getData();
        return R.ok(userTokenService.createToken(userRData));
    }

    /**
     * 激光 一键 登录/注册
     */
    @PostMapping("/jgAuthPhoneLogin")
    @ApiOperation(value = "激光 一键 登录/注册",notes = "成功返回业务token")
    public R<AccTokenVo> jgPhoneLogin(@RequestBodyRsa JgLoginTokenForm jgLoginTokenForm){
        R<CntUserDto> cntUserR = remoteBuiUserService.jgPhoneLogin(jgLoginTokenForm,SecurityConstants.INNER);
        Assert.isTrue(cntUserR.getCode() == CodeStatus.SUCCESS.getCode(),cntUserR.getMsg());
        CntUserDto userRData = cntUserR.getData();
        return R.ok(userTokenService.createToken(userRData));
    }

    /**
     * 用户退出
     */
    @GetMapping("/seeBy")
    @ApiOperation("用户退出登录")
    public R seeBy(){
        userTokenService.delLoginUser(SecurityUtils.getNotNullLoginBusinessUser());
        return R.ok();
    }



}
