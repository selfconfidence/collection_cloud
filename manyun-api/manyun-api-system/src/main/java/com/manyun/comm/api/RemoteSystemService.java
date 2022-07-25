package com.manyun.comm.api;

import com.manyun.comm.api.domain.dto.OpenPleaseBoxDto;
import com.manyun.comm.api.factory.RemoteBoxFallbackFactory;
import com.manyun.common.core.constant.SecurityConstants;
import com.manyun.common.core.constant.ServiceNameConstants;
import com.manyun.common.core.domain.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 业务板块 盲盒服务
 */
@FeignClient(contextId = "remoteSystemService", value = ServiceNameConstants.BUSINESS_SERVICE, fallbackFactory = RemoteBoxFallbackFactory.class)
public interface RemoteSystemService {



    /**
     * 根据TYPE 查询指定规则值
     */
    @GetMapping("/system/findType/{type}")
    R<String> findType(@PathVariable(name = "type") String type,@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}
