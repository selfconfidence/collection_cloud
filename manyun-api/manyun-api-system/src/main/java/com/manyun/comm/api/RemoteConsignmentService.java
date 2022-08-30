package com.manyun.comm.api;

import com.manyun.comm.api.domain.dto.OpenPleaseBoxDto;
import com.manyun.comm.api.factory.RemoteBoxFallbackFactory;
import com.manyun.comm.api.factory.RemoteConsignmentFallbackFactory;
import com.manyun.common.core.constant.SecurityConstants;
import com.manyun.common.core.constant.ServiceNameConstants;
import com.manyun.common.core.domain.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 业务板块 盲盒服务
 */
@FeignClient(contextId = "remoteConsignmentService", value = ServiceNameConstants.BUSINESS_SERVICE, fallbackFactory = RemoteConsignmentFallbackFactory.class)
public interface RemoteConsignmentService {

    @GetMapping("/consignment/cancelSchedulingConsignment")
     @ApiOperation("取消寄售市场中的资产")
     R cancelSchedulingConsignment(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);


     @GetMapping("/consignment/consignmentSuccess/{id}")
     @ApiOperation("审核通过;id = 寄售编号")
     R  consignmentSuccess(@PathVariable("id") String id,@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}
