package com.manyun.business.controller;


import cn.hutool.core.lang.Assert;
import com.manyun.business.service.ICntUserService;
import com.manyun.comm.api.domain.CntUser;
import com.manyun.comm.api.domain.vo.AccTokenVo;
import com.manyun.comm.api.model.LoginPhoneCodeForm;
import com.manyun.comm.api.model.LoginPhoneForm;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.redis.service.RedisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import static com.manyun.common.core.constant.BusinessConstants.RedisDict.PHONE_CODE;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@RestController
@RequestMapping("/cntUser")
@Api(tags = "用户相关apis")
public class CntUserController extends BaseController {

    @Autowired
    private ICntUserService userService;

    @Autowired
    private RedisService redisService;


    @PostMapping("/login")
    @ApiOperation(value = "用户登录",notes = "用户账号密码登录",hidden = true)
    public R<CntUser>  login(@RequestBody LoginPhoneForm loginPhoneForm){
         CntUser cntUser =  userService.login(loginPhoneForm);
        return R.ok(cntUser);
    }


    @PostMapping("/codeLogin")
    @ApiOperation(value = "用户验证码登录",notes = "验证码登录",hidden = true)
    public R<CntUser> codeLogin(@RequestBody LoginPhoneCodeForm loginPhoneCodeForm){
        String phoneCode = (String) redisService.redisTemplate.opsForValue().get(PHONE_CODE.concat(loginPhoneCodeForm.getPhone()));
        Assert.isTrue(loginPhoneCodeForm.getPhoneCode().equals(phoneCode),"验证码输入错误,请核实!");
        CntUser cntUser =   userService.codeLogin(loginPhoneCodeForm.getPhone());
        return R.ok(cntUser);
    }

}

