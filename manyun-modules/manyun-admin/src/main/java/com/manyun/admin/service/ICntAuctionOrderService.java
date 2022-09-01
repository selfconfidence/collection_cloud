package com.manyun.admin.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.admin.domain.CntAuctionOrder;
import com.manyun.admin.domain.query.AuctionOrderQuery;
import com.manyun.admin.domain.query.AuctionPriceQuery;
import com.manyun.admin.domain.vo.AuctionPriceVo;
import com.manyun.admin.domain.vo.CntAuctionOrderVo;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * 拍卖订单Service接口
 *
 * @author yanwei
 * @date 2022-09-01
 */
public interface ICntAuctionOrderService extends IService<CntAuctionOrder>
{

    /**
     * 查询拍卖订单列表
     *
     * @param auctionOrderQuery
     * @return 拍卖订单集合
     */
    public TableDataInfo<CntAuctionOrderVo> selectCntAuctionOrderList(AuctionOrderQuery auctionOrderQuery);

    /**
     * 竞价列表
     */
    TableDataInfo<AuctionPriceVo> auctionPriceList(AuctionPriceQuery auctionPriceQuery);
}
