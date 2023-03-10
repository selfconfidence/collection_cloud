package com.manyun.business.service;

import com.manyun.business.domain.dto.AuctionOrderCreateDto;
import com.manyun.business.domain.entity.AuctionOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.business.domain.query.AuctionOrderQuery;
import com.manyun.business.domain.vo.AuctionOrderVo;
import com.manyun.common.core.web.page.TableDataInfo;

import java.util.List;
import java.util.function.Consumer;

/**
 * <p>
 * 拍卖订单表 服务类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
public interface IAuctionOrderService extends IService<AuctionOrder> {



    /**
     * 我的拍卖订单
     * @param orderQuery
     * @param userId
     * @return
     */
    TableDataInfo<AuctionOrderVo> myAuctionOrderList(AuctionOrderQuery orderQuery, String userId);

    String createAuctionOrder(AuctionOrderCreateDto auctionOrderCreateDto, Consumer<String> consumer);

    List<AuctionOrder> checkUnpaidOrder(String payUserId);

    void notifyPaySuccess(String outHost);

    void timeCancelAuction();

    void cancelAuctionOrder(String id);

}
