package com.manyun.business.config;

import com.manyun.common.core.utils.jg.JgAuthLoginUtil;
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

    // 一键登录
    @Bean
    public JgAuthLoginUtil jgAuthLoginUtil(@Value("${jiguang.jpush.masterSecret}") String masterSecret,@Value("${jiguang.jpush.appKey}") String appKey,@Value("${jiguang.auth.priKey}") String priKey){
        return new JgAuthLoginUtil(masterSecret, appKey, priKey);
    }
}
