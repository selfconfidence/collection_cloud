package com.manyun.comm.api.factory;

import com.manyun.comm.api.RemoteAuctionService;
import com.manyun.common.core.domain.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class RemoteAuctionFallbackFactory implements FallbackFactory<RemoteAuctionService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteAuctionFallbackFactory.class);

    @Override
    public RemoteAuctionService create(Throwable throwable) {
        log.error("用户服务调用失败:{}", throwable.getMessage());
        return new RemoteAuctionService() {
            @Override
            public R timeCancelAuction(String source) {
                return R.fail("获取用户失败:" + throwable.getMessage());
            }

            @Override
            public R checkAuctionEnd(String source) {
                return null;
            }
            @Override
            public R timeStartAuction(String source) {
                return null;
            }

            @Override
            public R checkWinner(String source) {
                return null;
            }

            @Override
            public R checkDelayWinner(String source) {
                return null;
            }
        };
    }
}
