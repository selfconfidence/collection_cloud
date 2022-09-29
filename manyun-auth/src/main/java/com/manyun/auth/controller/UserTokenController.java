package com.manyun.auth.controller;

import cn.hutool.core.lang.Assert;
import com.dingxianginc.ctu.client.CaptchaClient;
import com.dingxianginc.ctu.client.model.CaptchaResponse;
import com.manyun.comm.api.RemoteBuiUserService;
import com.manyun.comm.api.domain.dto.CntUserDto;
import com.manyun.comm.api.domain.form.JgLoginTokenForm;
import com.manyun.comm.api.domain.vo.AccTokenVo;
import com.manyun.comm.api.model.LoginPhoneCodeForm;
import com.manyun.comm.api.model.LoginPhoneForm;
import com.manyun.common.core.annotation.RequestBodyRsa;
import com.manyun.common.core.constant.SecurityConstants;
import com.manyun.common.core.domain.CodeStatus;
import com.manyun.common.core.domain.R;
import com.manyun.common.security.service.UserTokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
    public R<AccTokenVo> login(@RequestBodyRsa @Valid LoginPhoneForm loginPhoneForm)
    {
        dingxiangTokenCheck(loginPhoneForm.getToken());
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
        dingxiangTokenCheck(loginPhoneCodeForm.getToken());
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


    private void dingxiangTokenCheck(String token){
        /**构造入参为appId和appSecret
         * appId和前端验证码的appId保持一致，appId可公开
         * appSecret为秘钥，请勿公开
         * token在前端完成验证后可以获取到，随业务请求发送到后台，token有效期为两分钟
         * ip 可选，提交业务参数的客户端ip
         **/
        String appId = "a0ab66c6771a8e8bd9da35c6788cfc63";
        String appSecret = "78cfa7486febe8a12a094db640c54592";
        CaptchaClient captchaClient = new CaptchaClient(appId,appSecret);
        captchaClient.setCaptchaUrl("https://cap-api.dingxiang-inc.com");
//指定服务器地址，saas可在控制台，应用管理页面最上方获取
        CaptchaResponse response = null;
        try {
            response = captchaClient.verifyToken(token);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
//CaptchaResponse response = captchaClient.verifyToken(token, ip);
//针对一些token冒用的情况，业务方可以采集客户端ip随token一起提交到验证码服务，验证码服务除了判断token的合法性还会校验提交业务参数的客户端ip和验证码颁发token的客户端ip是否一致
    //    System.out.println(response.getCaptchaStatus());
//确保验证状态是SERVER_SUCCESS，SDK中有容错机制，在网络出现异常的情况会返回通过
//System.out.println(response.getIp());
//验证码服务采集到的客户端ip
        Assert.isTrue(response.getResult(),"验证失败,请重试！");
    }

}
