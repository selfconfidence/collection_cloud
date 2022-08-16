package com.manyun.business.service;

import com.manyun.business.domain.entity.AuctionPrice;
import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.business.domain.form.AuctionPayFixedForm;
import com.manyun.business.domain.form.AuctionPayForm;
import com.manyun.business.domain.form.AuctionPayMarginForm;
import com.manyun.business.domain.form.AuctionPriceForm;
import com.manyun.business.domain.query.AuctionPriceQuery;
import com.manyun.business.domain.query.MyAuctionPriceQuery;
import com.manyun.business.domain.vo.*;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.web.page.TableDataInfo;

import java.math.BigDecimal;

/**
 * <p>
 * 竞价表 服务类
 * </p>
 *
 * @author 
 * @since 2022-06-30
 */
public interface IAuctionPriceService extends IService<AuctionPrice> {

    R<BigDecimal> myAuctionPrice(AuctionPriceForm auctionPriceForm, String userId);

    TableDataInfo<AuctionPriceVo> auctionPriceList(AuctionPriceQuery auctionPriceQuery);

    PayVo payAuction(String payUserId, AuctionPayForm auctionPayForm);

    PayVo payMargin(String payUserId, AuctionPayMarginForm auctionPayMarginForm);

    R checkPayMargin(AuctionPriceForm auctionPriceForm, String userId);

    void checkAuctionEnd();

    PayVo payFixed(String userId, AuctionPayFixedForm auctionPayFixedForm);

    TableDataInfo<MyAuctionPriceVo> myAuctionPriceList(MyAuctionPriceQuery myAuctionPriceQuery, String userId);

    AuctionCollectionAllVo priceCollectionInfo(String collectionId, String auctionSendId);

    AuctionBoxAllVo priceBoxInfo(String boxId, String auctionSendId);

    void checkWinner();

    void checkDelayWinner();
}
