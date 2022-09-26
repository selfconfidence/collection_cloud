package com.manyun.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPathPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

/*
 * 限流配置中心
 *
 * @author yanwei
 * @create 2022-09-26
 */
@Configuration
public class SentinelConfig {


    @PostConstruct
    private void initCustomizedApis(){
        Set<ApiDefinition> definitions = new HashSet<>();
        ApiDefinition businessApis = new ApiDefinition("manyun-business_apis")
                .setPredicateItems(new HashSet<ApiPredicateItem>() {{
                    add(new ApiPathPredicateItem().setPattern("/business/collection/sellOrderCollection"));
                    add(new ApiPathPredicateItem().setPattern("/business/collection/pageList")
                            .setMatchStrategy(SentinelGatewayConstants.PARAM_MATCH_STRATEGY_PREFIX));
                }});
        definitions.add(businessApis);
        GatewayApiDefinitionManager.loadApiDefinitions(definitions);
    }
}
