package com.manyun.business.controller;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.manyun.business.domain.entity.CntUser;
import com.manyun.business.domain.form.UserChangeForm;
import com.manyun.business.domain.form.UserChangeLoginForm;
import com.manyun.business.domain.form.UserChangePayPass;
import com.manyun.business.domain.form.UserRealForm;
import com.manyun.business.domain.vo.UserInfoVo;
import com.manyun.business.domain.vo.UserLevelVo;
import com.manyun.business.domain.vo.UserPleaseBoxVo;
import com.manyun.business.service.ICntUserService;
import com.manyun.comm.api.domain.dto.CntUserDto;
import com.manyun.comm.api.model.LoginBusinessUser;
import com.manyun.comm.api.model.LoginPhoneCodeForm;
import com.manyun.comm.api.model.LoginPhoneForm;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.redis.service.RedisService;
import com.manyun.common.security.annotation.InnerAuth;
import com.manyun.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

import java.util.List;
import java.util.Objects;

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
    @InnerAuth
    public R<CntUserDto>  login(@RequestBody LoginPhoneForm loginPhoneForm){
         CntUser cntUser =  userService.login(loginPhoneForm);
        CntUserDto cntUserDto = Builder.of(CntUserDto::new).build();
        BeanUtil.copyProperties(cntUser,cntUserDto);
        return R.ok(cntUserDto);
    }


    @PostMapping("/codeLogin")
    @ApiOperation(value = "用户验证码登录",notes = "验证码登录",hidden = true)
    @InnerAuth
    public R<CntUserDto> codeLogin(@RequestBody LoginPhoneCodeForm loginPhoneCodeForm){
        String phoneCode = (String) redisService.redisTemplate.opsForValue().get(PHONE_CODE.concat(loginPhoneCodeForm.getPhone()));
        Assert.isTrue(loginPhoneCodeForm.getPhoneCode().equals(phoneCode),"验证码输入错误,请核实!");
        CntUser cntUser =   userService.codeLogin(loginPhoneCodeForm.getPhone());
        CntUserDto cntUserDto = Builder.of(CntUserDto::new).build();
        BeanUtil.copyProperties(cntUser,cntUserDto);
        return R.ok(cntUserDto);
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
        //TODO 实名认证未完善


        return R.ok();
    }

    // 修改登录密码
    @PostMapping("/changeLogin")
    @ApiOperation("修改登录密码")
    public R changeLogin(@RequestBody @Valid UserChangeLoginForm userChangeLoginForm){
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        userService.changeLogin(notNullLoginBusinessUser.getUserId(),userChangeLoginForm);
        return R.ok();
    }
    // 修改支付密码
    @PostMapping("/changePayPass")
    @ApiOperation("修改支付密码")
    public R changePayPass(@RequestBody @Valid UserChangePayPass userChangePayPass){
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        String phone = notNullLoginBusinessUser.getCntUser().getPhone();
        String phoneCode = (String) redisService.redisTemplate.opsForValue().get(PHONE_CODE.concat(phone));
        Assert.isTrue(userChangePayPass.getPhoneCode().equals(phoneCode),"验证码输入错误,请核实!");
        userService.changePayPass(notNullLoginBusinessUser.getUserId(),userChangePayPass);
        return R.ok();
    }

    @GetMapping("/userLevel")
    @ApiOperation("查看我的下级人数")
    public R<UserLevelVo> userLevel(){
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        return R.ok(userService.userLevel(notNullLoginBusinessUser.getUserId()));
    }


    @GetMapping("/userPleaseBox")
    @ApiOperation("邀请盲盒奖励规则 下级实名才可以")
    public R<List<UserPleaseBoxVo>> userPleaseBox(){
        String userId = SecurityUtils.getNotNullLoginBusinessUser().getUserId();
        List<UserPleaseBoxVo> userPleaseBoxVos =  userService.userPleaseBoxVo(userId);
        return R.ok(userPleaseBoxVos);
    }


    @GetMapping("/openPleaseBox/{id}")
    @ApiOperation(value = "邀请奖励进行领取 id 为领取编号",notes = "返回的 data 是消息提示,给用户看即可!")
    public R<String> openPleaseBox(@PathVariable String id){
        String userId = SecurityUtils.getNotNullLoginBusinessUser().getUserId();
        String msg = userService.openPleaseBox(userId,id);
        return R.ok(msg);

    }

    /**
     * 根据用户的  手机号|区块链地址|ID|系统编号 查询用户信息
     */
    @GetMapping("/commUni/{commUni}")
    @InnerAuth
    @ApiOperation(value = "根据用户的  手机号|区块链地址|ID 查询用户信息",hidden = true)
    public R<CntUserDto> commUni(@PathVariable(name = "commUni") String commUni){
        CntUser cntUser = userService.commUni(commUni);
        if (Objects.isNull(cntUser)) return R.ok();
        CntUserDto cntUserDto = Builder.of(CntUserDto::new).build();
        BeanUtil.copyProperties(cntUser,cntUserDto);
        return R.ok(cntUserDto);
    }




}

