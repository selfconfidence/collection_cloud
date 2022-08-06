package com.manyun.business.service;

import com.manyun.business.domain.entity.AuctionSend;
import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.business.domain.form.AuctionSendForm;
import com.manyun.business.domain.query.AuctionMarketQuery;
import com.manyun.business.domain.query.AuctionSendQuery;
import com.manyun.business.domain.vo.*;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.web.page.PageQuery;
import com.manyun.common.core.web.page.TableDataInfo;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 拍卖表 服务类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
public interface IAuctionSendService extends IService<AuctionSend> {

    R auctionSend(AuctionSendForm auctionSendForm, String userId);

    TableDataInfo<MyAuctionSendVo> pageList(AuctionSendQuery sendQuery, String userId);

    TableDataInfo<AuctionMarketVo> auctionMarketList(AuctionMarketQuery marketQuery);

    void reloadAuctionSend(List<AuctionSend> auctionSendList);

    AuctionCollectionAllVo auctionCollectionInfo(String collectionId, String auctionSendId);

    AuctionBoxAllVo auctionBoxInfo(String boxId, String auctionSendId);

    void timeStartAuction();

    R reAuctionSend(AuctionSendForm auctionSendForm, String auctionSendId);

    AuctionVo getAuctionSendVo(String auctionSendId);

    List<KeywordVo> queryDict(String keyword);

    CollectionInfoVo getBaseCollectionInfoVo(String collectionId);

    R<BigDecimal> auctionSendConfig();

    R<BigDecimal> auctionSendCommission();

}
