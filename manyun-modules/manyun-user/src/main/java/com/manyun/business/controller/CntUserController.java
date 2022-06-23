package com.manyun.business.controller;


import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.manyun.business.domain.form.UserChangeForm;
import com.manyun.business.domain.form.UserRealForm;
import com.manyun.business.domain.vo.UserInfoVo;
import com.manyun.business.service.ICntUserService;
import com.manyun.comm.api.domain.CntUser;
import com.manyun.comm.api.domain.vo.AccTokenVo;
import com.manyun.comm.api.model.LoginBusinessUser;
import com.manyun.comm.api.model.LoginPhoneCodeForm;
import com.manyun.comm.api.model.LoginPhoneForm;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.redis.service.RedisService;
import com.manyun.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.validation.Valid;

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

    @PostMapping("/changeUser")
    @ApiOperation("修改个人信息")
    public R changeUser(@RequestBody UserChangeForm userChangeForm){
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        userService.changeUser(userChangeForm,notNullLoginBusinessUser.getUserId());
        return R.ok();
    }

    @GetMapping("/info")
    @ApiOperation("查询用户的详细信息")
    public R<UserInfoVo> info(){

        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        // 详细信息
        UserInfoVo userInfoVo = userService.info(notNullLoginBusinessUser.getUserId());
        return R.ok(userInfoVo);
    }

    // 实名认证
    @PostMapping("/realUser")
    @ApiOperation("实名认证")
    public R realUser(@RequestBody @Valid UserRealForm userRealForm){

        return R.ok();
    }

    // 修改登录密码



    // 修改支付密码





}

