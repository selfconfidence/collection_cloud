package com.manyun.comm.api;

import com.manyun.comm.api.domain.dto.BoxListDto;
import com.manyun.comm.api.domain.dto.OpenPleaseBoxDto;
import com.manyun.comm.api.factory.RemoteBoxFallbackFactory;
import com.manyun.comm.api.factory.RemoteTarFallbackFactory;
import com.manyun.common.core.constant.SecurityConstants;
import com.manyun.common.core.constant.ServiceNameConstants;
import com.manyun.common.core.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 业务板块 抽签服务
 */
@FeignClient(contextId = "remoteTarService", value = ServiceNameConstants.BUSINESS_SERVICE, fallbackFactory = RemoteTarFallbackFactory.class)
public interface RemoteTarService {

    /**
     * 公布开奖结果
     */
    @GetMapping("/tar/taskEndFlag")
    R taskEndFlag(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}
