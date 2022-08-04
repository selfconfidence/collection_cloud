package com.manyun.comm.api;


import com.manyun.comm.api.factory.RemoteAuctionFallbackFactory;
import com.manyun.common.core.constant.SecurityConstants;
import com.manyun.common.core.constant.ServiceNameConstants;
import com.manyun.common.core.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(contextId = "remoteAuctionService", value = ServiceNameConstants.BUSINESS_SERVICE, fallbackFactory = RemoteAuctionFallbackFactory.class)
public interface RemoteAuctionService {

    /**
     * 定时调度取消未支付的拍卖订单
     *
     * @return 结果
     */
    @GetMapping("/auctionOrder/timeCancelAuction")
    R timeCancelAuction(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    @GetMapping("/auctionOrder/checkAuctionEnd")
    R checkAuctionEnd(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    @GetMapping("/auctionSend/timeStartAuction")
    R timeStartAuction(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    @GetMapping("/auctionPrice/checkWinner")
    R checkWinner(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
