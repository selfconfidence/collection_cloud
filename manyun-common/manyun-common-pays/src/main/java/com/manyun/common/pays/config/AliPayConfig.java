package com.manyun.common.pays.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
@ConfigurationProperties(prefix = "ali.pay")
@Data
public class AliPayConfig {

    /**
     * 回调baseURL
     */
    public String webUrl;

    /**
     * 支付宝，appId
     */
    public String aliAppId;

    /**
     * 支付宝，公钥
     */
    public String aliPublicKey;

    /**
     * 支付宝，个人密钥
     */
    public String aliPrivateKey;

}
