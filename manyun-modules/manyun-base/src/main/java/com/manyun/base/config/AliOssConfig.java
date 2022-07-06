package com.manyun.base.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "oss")
@Data
public class AliOssConfig {


    private String bucketName;

    private String accessKey;

    private String secret;

    private String endpoint;

    private String bucketDomain;

    private String fileName;

}
