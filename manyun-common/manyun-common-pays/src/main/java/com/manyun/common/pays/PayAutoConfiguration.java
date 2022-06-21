package com.manyun.common.pays;

import com.manyun.common.pays.abs.impl.AliComm;
import com.manyun.common.pays.abs.impl.WxComm;
import com.manyun.common.pays.config.AliPayConfig;
import com.manyun.common.pays.config.WechatPayConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PayAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AliPayConfig aliPayConfig()
    {
        return new AliPayConfig();
    }


    @Bean
    @ConditionalOnMissingBean
    public WechatPayConfig wechatPayConfig()
    {
        return new WechatPayConfig();
    }


    @Bean
    public AliComm aliComm(AliPayConfig aliPayConfig){
        return new AliComm(aliPayConfig);
    }


    @Bean
    public WxComm wechatComm(WechatPayConfig wechatPayConfig){
        return new WxComm(wechatPayConfig);
    }
}
