package com.manyun.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 网关启动程序
 * 
 * @author ruoyi
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@Slf4j
public class ManyunGatewayApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(ManyunGatewayApplication.class, args);
        log.info("网关启动成功");
    }
}
