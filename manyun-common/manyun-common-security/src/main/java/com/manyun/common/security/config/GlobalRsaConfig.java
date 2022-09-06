package com.manyun.common.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/*
 * 全局参数加密
 *
 * @author yanwei
 * @create 2022-09-05
 */
@Configuration
@ConfigurationProperties(prefix = "global")
@Data
@RefreshScope
public class GlobalRsaConfig {

    private String projectNames[];

    private String privateKey;
    private String publicKey;

    private Duration requestTime;

    private String errorMsg;

}
