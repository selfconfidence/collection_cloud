package com.manyun.base.controller;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.RandomUtil;
import com.manyun.common.core.domain.R;
import com.manyun.common.redis.service.RedisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.manyun.common.core.constant.BusinessConstants.RedisDict.EXP_TIME;
import static com.manyun.common.core.constant.BusinessConstants.RedisDict.PHONE_CODE;


@RestController
@Api(tags = "公众相关服务")
public class BaseController {

    @Autowired
    private RedisService redisService;

    @GetMapping("/sendPhone/{phone}")
    @ApiOperation(value = "发送验证码",notes = "测试阶段返回 code 编码")
    public R<String> sendPhone(@PathVariable String phone){
        Object cacheObject = redisService.redisTemplate.opsForValue().get(PHONE_CODE.concat(phone));
        Assert.isTrue(Objects.isNull(cacheObject),"未过期,请稍后重试!");
        // 发送验证码
        String code = RandomUtil.randomString(6);
        redisService.redisTemplate.opsForValue().set(PHONE_CODE.concat(phone),code,EXP_TIME, TimeUnit.MINUTES);
        return R.ok(code);
    }
}
