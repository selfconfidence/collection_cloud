package com.manyun.business.controller;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson2.JSONObject;
import com.manyun.business.config.AliRealConfig;
import com.manyun.business.domain.entity.CntUser;
import com.manyun.business.domain.form.*;
import com.manyun.business.domain.vo.*;
import com.manyun.business.service.ICntUserService;
import com.manyun.comm.api.domain.dto.CntUserDto;
import com.manyun.comm.api.domain.form.JgLoginTokenForm;
import com.manyun.comm.api.domain.vo.AccTokenVo;
import com.manyun.comm.api.model.LoginBusinessUser;
import com.manyun.comm.api.model.LoginPhoneCodeForm;
import com.manyun.comm.api.model.LoginPhoneForm;
import com.manyun.common.core.annotation.RequestBodyRsa;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.enums.PhoneCodeType;
import com.manyun.common.core.utils.StringUtils;
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


    @PostMapping("/regUser")
    @ApiOperation("用户注册 - 手机号验证码的方式")
    public R regUser(@RequestBody @Valid UserRegForm userRegForm){
        String phoneCode = (String) redisService.redisTemplate.opsForValue().get(PHONE_CODE.concat(userRegForm.getPhone().concat(PhoneCodeType.REG_CODE.getType())));
        Assert.isTrue(userRegForm.getPhoneCode().equals(phoneCode),"验证码输入错误,请核实!");
        userService.regUser(userRegForm);
        return R.ok();
    }


    @PostMapping("/codeLogin")
    @ApiOperation(value = "用户验证码登录",notes = "验证码登录",hidden = true)
    @InnerAuth
    public R<CntUserDto> codeLogin(@RequestBody LoginPhoneCodeForm loginPhoneCodeForm){
        codeCheck(loginPhoneCodeForm.getPhone(),loginPhoneCodeForm.getPhoneCode(), PhoneCodeType.LOGIN_CODE.getType());
        CntUser cntUser =   userService.codeLogin(loginPhoneCodeForm.getPhone());
        CntUserDto cntUserDto = Builder.of(CntUserDto::new).build();
        BeanUtil.copyProperties(cntUser,cntUserDto);
        return R.ok(cntUserDto);
    }

    private void codeCheck(String phone,String code, String type) {
        Object andDelete = redisService.redisTemplate.opsForValue().get(PHONE_CODE.concat(phone.concat(type)));
        Assert.isTrue(Objects.nonNull(andDelete),"验证码失效！");
        String phoneCode = andDelete.toString();
        Assert.isTrue(code.equals(phoneCode),"验证码输入错误,请核实!");

    }

    @PostMapping("/changeUser")
    @ApiOperation("修改个人信息")
    public R changeUser(@RequestBodyRsa UserChangeForm userChangeForm){
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
    @ApiOperation("实名认证 -- 银联")
    public R realUser(@RequestBodyRsa @Valid UserRealForm userRealForm){
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        codeCheck(userRealForm.getPhone(),userRealForm.getPhoneCode(), PhoneCodeType.REAL_CODE.getType());
        return userService.userRealName(userRealForm, notNullLoginBusinessUser.getUserId());
    }

    //调起认证
   @PostMapping("/getCertifyId")
   @ApiOperation(value = "获取认证ID -app",notes = "app 端")
   public R<String> getCertifyId(@RequestBodyRsa UserAliyunRealForm userAliyunRealForm){
       LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
       return R.ok(userService.getCertifyId(notNullLoginBusinessUser.getCntUser(),userAliyunRealForm));
   }

   @GetMapping("/checkCertifyIdStatus/{certifyId}")
   @ApiOperation(value = "查询阿里云刷脸实名认证信息 - app",notes = "app 端")
   public R checkCertifyIdStatus(@PathVariable String certifyId){
       LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
       userService.checkCertifyIdStatus(certifyId,notNullLoginBusinessUser.getCntUser());
        return R.ok();
   }


    //调起认证
    @PostMapping("/getH5CertifyId")
    @ApiOperation(value = "获取认证ID -h5",notes = "h5 端 \n version 1.0.1")
    public R<AliRealVo> getH5CertifyId(@RequestBodyRsa UserAliyunRealForm userAliyunRealForm){
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        return R.ok(userService.getH5CertifyId(userAliyunRealForm,notNullLoginBusinessUser.getUserId()));
    }

    @GetMapping("/checkCertifyIdH5Status/{certifyId}")
    @ApiOperation(value = "查询阿里云刷脸实名认证信息 - h5",notes = "h5 端 \n version 1.0.1")
    public R checkCertifyIdH5Status(@PathVariable String certifyId){
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        userService.checkCertifyIdH5Status(certifyId,notNullLoginBusinessUser.getCntUser().getId());
        return R.ok();
    }
    // 修改登录密码
    @PostMapping("/changeLogin")
    @ApiOperation("修改登录密码--废弃")
    @Deprecated
    public R changeLogin(@RequestBody @Valid UserChangeLoginForm userChangeLoginForm){
        Assert.isTrue(false,"changeLogin is deprecated ==> change changeCodeLogin newApi");
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        userService.changeLogin(notNullLoginBusinessUser.getUserId(),userChangeLoginForm);
        return R.ok();
    }

    // 修改登录密码
    @PostMapping("/changeCodeLogin")
    @ApiOperation("修改登录密码")
    public R changeCodeLogin(@RequestBodyRsa @Valid UserChangeCodeLoginForm userChangeCodeLoginForm){
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        String phone = notNullLoginBusinessUser.getCntUser().getPhone();
        codeCheck(phone,userChangeCodeLoginForm.getPhoneCode(), PhoneCodeType.CHANGE_LOGIN_PASS.getType());
        CntUser cntUser = userService.getById(notNullLoginBusinessUser.getUserId());
        cntUser.setLoginPass(userChangeCodeLoginForm.getPassWord());
        userService.updateById(cntUser);
        return R.ok();
    }
    // 修改支付密码
    @PostMapping("/changePayPass")
    @ApiOperation(value = "修改支付密码")
    public R changePayPass(@RequestBody @Valid UserChangePayPass userChangePayPass){
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        String phone = notNullLoginBusinessUser.getCntUser().getPhone();
        codeCheck(phone,userChangePayPass.getPhoneCode(), PhoneCodeType.CHANGE_PAY_PASS.getType());
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
    public synchronized R<String> openPleaseBox(@PathVariable String id){
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

    @PostMapping("/jgPhoneLogin")
    @ApiOperation(value = "激光 一键 登录/注册",notes = "成功返回业务token",hidden = true)
    @InnerAuth
    public R<CntUserDto> jgPhoneLogin(@RequestBody JgLoginTokenForm jgLoginTokenForm){
        CntUser cntUser = userService.jgPhoneLogin(jgLoginTokenForm);
        CntUserDto cntUserDto = Builder.of(CntUserDto::new).build();
        BeanUtil.copyProperties(cntUser,cntUserDto);
          return   R.ok(cntUserDto);
    }


    @PostMapping("/inviteUser")
    @ApiOperation(value = "分享邀请海报")
    public R<InviteUserVo> inviteUser() {
        String userId = SecurityUtils.getNotNullLoginBusinessUser().getUserId();
        return userService.inviteUser(userId);
    }

    @PostMapping("/asyncInviteUser")
    @ApiOperation(value = "异步生成邀请海报")
    public R asyncInviteUser() {
        String userId = SecurityUtils.getNotNullLoginBusinessUser().getUserId();
        userService.asyncInviteUser(userId);
        return R.ok();
    }

    @PostMapping("/checkPaySecure")
    @ApiOperation(value = "检查支付密码是否一致",notes = "{\"paySecure\":\"123321123\"}")
    public R checkPaySecure(@RequestBodyRsa JSONObject jsonObject){
        String paySecure = jsonObject.getString("paySecure");
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        userService.checkPaySecure(paySecure,notNullLoginBusinessUser.getUserId());
        return R.ok();
    }


    @PostMapping("/saveJpush")
    @ApiOperation(value = "保存用户激光推送的 uuid",notes = "{\"uuId\":\"123jshadjhsa123\"}")
    public R saveJpush(@RequestBody JSONObject jsonObject){
        String uuId = null;
        Assert.isTrue(StringUtils.isNotBlank((uuId = jsonObject.getString("uuId"))),"uuId 不可为空");
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        userService.saveJpush(notNullLoginBusinessUser.getUserId(),uuId);
        return R.ok();
    }




}

