package com.manyun.business.config;

import com.manyun.common.core.utils.jg.JpushUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GenderConfig {
  // 推送能力
    @Bean
    public JpushUtil jpushUtil(@Value("${jiguang.jpush.masterSecret}") String masterSecret,@Value("${jiguang.jpush.appKey}") String appKey){
        return new JpushUtil(masterSecret, appKey);
    }
}
