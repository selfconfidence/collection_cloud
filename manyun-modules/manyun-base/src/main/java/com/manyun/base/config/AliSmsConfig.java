package com.manyun.base.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "sms")
@Data
public class AliSmsConfig {


    private String templateCode;

    private String accessKey;

    private String secret;

    private String sing;

    private String regionId;
}
