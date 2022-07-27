package com.manyun.comm.api;

import com.manyun.comm.api.factory.RemoteChainxFallbackFactory;
import com.manyun.common.core.constant.SecurityConstants;
import com.manyun.common.core.constant.ServiceNameConstants;
import com.manyun.common.core.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 业务板块 蚂蚁链服务
 */
@FeignClient(contextId = "remoteChainxService", value = ServiceNameConstants.BUSINESS_SERVICE, fallbackFactory = RemoteChainxFallbackFactory.class)
public interface MyChainxSystemService {



    /**
     * 重试上链接口
     * @param userId  用户编号
     * @param userCollectionId  未上链成功的 用户和藏品中间表ID
     */
     @GetMapping("/mychain/resetUpLink")
     R resetUpLink(@RequestParam("userId") String userId, @RequestParam("userCollectionId") String userCollectionId,@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}
