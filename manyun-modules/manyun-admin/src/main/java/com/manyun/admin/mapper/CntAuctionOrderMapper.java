package com.manyun.admin.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.admin.domain.CntAuctionOrder;
import com.manyun.admin.domain.query.AuctionOrderQuery;
import com.manyun.admin.domain.vo.CntAuctionOrderVo;

/**
 * 拍卖订单Mapper接口
 *
 * @author yanwei
 * @date 2022-09-01
 */
public interface CntAuctionOrderMapper extends BaseMapper<CntAuctionOrder>
{
    /**
     * 查询拍卖订单列表
     *
     * @param auctionOrderQuery 拍卖订单
     * @return 拍卖订单集合
     */
    public List<CntAuctionOrderVo> selectCntAuctionOrderList(AuctionOrderQuery auctionOrderQuery);
}
