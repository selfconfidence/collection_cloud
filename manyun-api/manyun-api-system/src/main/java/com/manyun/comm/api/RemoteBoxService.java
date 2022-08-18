package com.manyun.comm.api;

import com.manyun.comm.api.domain.dto.BoxListDto;
import com.manyun.comm.api.domain.dto.OpenPleaseBoxDto;
import com.manyun.comm.api.factory.RemoteBoxFallbackFactory;
import com.manyun.common.core.constant.SecurityConstants;
import com.manyun.common.core.constant.ServiceNameConstants;
import com.manyun.common.core.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 业务板块 盲盒服务
 */
@FeignClient(contextId = "remoteBoxService", value = ServiceNameConstants.BUSINESS_SERVICE, fallbackFactory = RemoteBoxFallbackFactory.class)
public interface RemoteBoxService {

    /**
     * 绑定盲盒
     */
    @PostMapping("/box/openPleaseBox")
    R openPleaseBox(@RequestBody OpenPleaseBoxDto pleaseBoxDto,@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 查询盲盒详细信息
     */
    @GetMapping("/box/innerInfo/{id}")
    R<BoxListDto> innerInfo(@PathVariable("id") String id,@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}
