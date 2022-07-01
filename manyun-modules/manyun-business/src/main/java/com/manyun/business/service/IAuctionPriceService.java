package com.manyun.business.service;

import com.manyun.business.domain.entity.AuctionPrice;
import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.business.domain.form.AuctionPriceForm;
import com.manyun.business.domain.vo.AuctionPriceVo;
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

    R myAuctionPrice(AuctionPriceForm auctionPriceForm);

    //TableDataInfo<AuctionPriceVo> auctionPriceList(AuctionPrice auctionPrice);
}
