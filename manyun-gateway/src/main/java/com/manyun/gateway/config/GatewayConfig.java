package com.manyun.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPathPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.SentinelGatewayFilter;
import com.manyun.gateway.config.properties.SentinelFlowProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import com.manyun.gateway.handler.SentinelFallbackHandler;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.support.DefaultServerCodecConfigurer;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 网关限流配置
 * 
 * @author ruoyi
 */
@Configuration
@RefreshScope
public class GatewayConfig
{

    @Autowired
    private SentinelFlowProperties sentinelFlowProperties;
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SentinelFallbackHandler sentinelGatewayExceptionHandler()
    {
        return new SentinelFallbackHandler();
    }

    @Bean
    @Order(-1)
    public GlobalFilter sentinelGatewayFilter() {
        return new SentinelGatewayFilter();
    }

    @PostConstruct
    private void initCustomizedApis(){
        List<SentinelFlowProperties.FlowNode> flowNodeLists = sentinelFlowProperties.getFlowNodeLists();
        if (flowNodeLists.isEmpty())return;
        Set<ApiDefinition> definitions = new HashSet<>();//new ApiPathPredicateItem().setPattern("/business/collection/sellOrderCollection")
        for (SentinelFlowProperties.FlowNode flowNode : flowNodeLists) {
            ApiDefinition commApis = new ApiDefinition(flowNode.getSourceName())
                    .setPredicateItems(flowNode.getPathUrl().parallelStream().map(item -> new ApiPathPredicateItem().setPattern(item)).collect(Collectors.toSet()));
            definitions.add(commApis);
        }
        GatewayApiDefinitionManager.loadApiDefinitions(definitions);
    }
}