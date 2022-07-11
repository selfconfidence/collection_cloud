package com.manyun.business.service;

import com.manyun.business.domain.entity.AuctionPrice;
import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.business.domain.form.AuctionPayForm;
import com.manyun.business.domain.form.AuctionPriceForm;
import com.manyun.business.domain.query.AuctionPriceQuery;
import com.manyun.business.domain.query.MyAuctionPriceQuery;
import com.manyun.business.domain.vo.AuctionPriceVo;
import com.manyun.business.domain.vo.MyAuctionPriceVo;
import com.manyun.business.domain.vo.PayVo;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * <p>
 * 竞价表 服务类
 * </p>
 *
 * @author 
 * @since 2022-06-30
 */
public interface IAuctionPriceService extends IService<AuctionPrice> {

    R myAuctionPrice(AuctionPriceForm auctionPriceForm, String userId);

    TableDataInfo<AuctionPriceVo> auctionPriceList(AuctionPriceQuery auctionPriceQuery);

    PayVo payAuction(String payUserId, AuctionPayForm auctionPayForm);

    PayVo payMargin(String payUserId, AuctionPayForm auctionPayForm);

    R checkPayMargin(AuctionPriceForm auctionPriceForm, String userId);

    void checkAuctionEnd();

    PayVo payFixed(String userId, AuctionPayForm auctionPayForm);

    TableDataInfo<MyAuctionPriceVo> myAuctionPriceList(MyAuctionPriceQuery myAuctionPriceQuery, String userId);
}
