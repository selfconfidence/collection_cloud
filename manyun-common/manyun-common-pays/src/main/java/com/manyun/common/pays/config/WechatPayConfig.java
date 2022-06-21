package com.manyun.common.pays.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
@ConfigurationProperties(prefix = "wechat.pay")
@Data
public class WechatPayConfig {

    /**
     * 回调baseURL
     */
    public String webUrl;

    /**
     * 微信 app ID
     */
    public String wxAppId;

    /**
     * 微信 商户ID
     */
    public String wxMchId;

    /**
     * 微信 证书地址
     */
    public String wxCertAddr;

    /**
     * 微信，商户KEY
     */
    public String wxMchKey;

}
