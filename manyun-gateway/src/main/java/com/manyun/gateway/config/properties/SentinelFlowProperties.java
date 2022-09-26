package com.manyun.gateway.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * sentinel 流控组
 *
 * @author ruoyi
 */
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "sentinel.flow")
@Data
public class SentinelFlowProperties
{
    private List<FlowNode> flowNodeLists;


    @Data
    public static class FlowNode{
        private String sourceName;
        private List<String> pathUrl;
    }
}
