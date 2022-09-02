package com.manyun.base.controller;

import cn.hutool.core.util.RandomUtil;
import com.manyun.base.utils.AliUtil;
import com.manyun.comm.api.domain.dto.SmsCommDto;
import com.manyun.common.core.domain.CodeStatus;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.utils.StringUtils;
import com.manyun.common.redis.service.RedisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.manyun.common.core.constant.BusinessConstants.RedisDict.EXP_TIME;
import static com.manyun.common.core.constant.BusinessConstants.RedisDict.PHONE_CODE;


@RestController
@Api(tags = "公众相关服务")
public class BaseController {

    @Autowired
    private RedisService redisService;

    @GetMapping("/sendPhone/{phone}/{type}")
    @ApiOperation(value = "发送验证码",notes = "测试阶段返回 code 编码,登录验证码=login_code，实名验证码=real_code，" +
            "注册验证码=reg_code，修改登录密码=change_login_pass，修改支付密码=change_pay_pass")
    public R<String> sendPhone(@PathVariable String phone, @PathVariable String type){
        if (StringUtils.isBlank(type)) {
            return R.fail("验证码类型不能为空");
        }
        Object cacheObject = redisService.redisTemplate.opsForValue().get(PHONE_CODE.concat(phone).concat(type));
        if (Objects.nonNull(cacheObject))
            return R.fail(CodeStatus.PLEASE_WAIT.getCode().intValue(),"未过期,请稍后重试!");
        // 发送验证码
        String code = RandomUtil.randomNumbers(6);
        AliUtil.sendSms(phone,code);
        //AliUtil.send(phone, BusinessConstants.SmsTemplateNumber.ASSERT_BACK)
        redisService.redisTemplate.opsForValue().set(PHONE_CODE.concat(phone).concat(type),code,EXP_TIME, TimeUnit.SECONDS);
        return R.ok();
    }

    @PostMapping("/sendCommPhone")
    @ApiOperation("内部提供短信通知")
    public R sendCommPhone(@RequestBody SmsCommDto smsCommDto){
        AliUtil.send(smsCommDto.getPhoneNumber(), smsCommDto.getTemplateCode(), smsCommDto.getParamsMap());
        return R.ok();
    }
}
