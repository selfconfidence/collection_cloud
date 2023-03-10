package com.manyun.business;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.manyun.common.security.annotation.EnableCustomConfig;
import com.manyun.common.security.annotation.EnableManyunFeignClients;
import com.manyun.common.swagger.annotation.EnableCustomSwagger2;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 *
 * 
 * @author
 */
@EnableCustomConfig    //  具体业务解析注解 业务模块必备
@EnableCustomSwagger2    // 在线文档注册，业务模块必备
@EnableManyunFeignClients    // 远程feign 调用注解
@SpringBootApplication
@EnableAsync
@EnableRetry
public class ManyunBusinessApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(ManyunBusinessApplication.class, args);
    }
}
