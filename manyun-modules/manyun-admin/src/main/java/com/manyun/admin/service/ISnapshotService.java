package com.manyun.admin.service;

import com.manyun.admin.domain.query.*;
import com.manyun.admin.domain.vo.*;
import com.manyun.common.core.web.page.TableDataInfo;

import java.util.List;

/**
 * 快照
 * @author yanwei
 * @date 2022-08-19
 */
public interface ISnapshotService {

    /**
     * 规定时间统计商品查询
     * @param statisticalGoodsQuery
     * @return
     */
    TableDataInfo<StatisticalGoodsVo> statisticalGoods(StatisticalGoodsQuery statisticalGoodsQuery);

    /**
     * 规定时间统计商品数据导出
     * @param statisticalGoodsQuery
     * @return
     */
    List<StatisticalGoodsVo> statisticalGoodsExcel(StatisticalGoodsQuery statisticalGoodsQuery);

    /**
     * 藏品编号查询
     * @param collectionNumberQuery
     * @return
     */
    TableDataInfo<CollectionNumberVo> collectionNumberQuery(CollectionNumberQuery collectionNumberQuery);

    /**
     * 藏品编号数据导出
     * @param collectionNumberQuery
     * @return
     */
    List<CollectionNumberVo> collectionNumberExcel(CollectionNumberQuery collectionNumberQuery);

    /**
     * 规定时间藏品交易量及交易金额查询
     * @param collectionBusinessQuery
     * @return
     */
    TableDataInfo<CollectionBusinessVo> collectionBusinessQuery(CollectionBusinessQuery collectionBusinessQuery);

    /**
     * 规定时间藏品交易量及交易金额数据导出
     * @param collectionBusinessQuery
     * @return
     */
    List<CollectionBusinessVo> collectionBusinessExcel(CollectionBusinessQuery collectionBusinessQuery);

    /**
     * 规定时间指定藏品买卖查询
     * @param collectionSalesStatisticsQuery
     * @return
     */
    TableDataInfo<CollectionSalesStatisticsVo> collectionSalesStatistics(CollectionSalesStatisticsQuery collectionSalesStatisticsQuery);


    /**
     * 规定时间指定藏品买卖数据导出
     * @param collectionSalesStatisticsQuery
     * @return
     */
    List<CollectionSalesStatisticsVo> collectionSalesStatisticsExcel(CollectionSalesStatisticsQuery collectionSalesStatisticsQuery);


    /**
     * 规定时间指定一件或多件藏品的持有总量查询
     * @param collectionTotalNumberQuery
     * @return
     */
    TableDataInfo<CollectionTotalNumberVo> collectionTotalNumber(CollectionTotalNumberQuery collectionTotalNumberQuery);


    /**
     * 规定时间指定一件或多件藏品的持有总量数据导出
     * @param collectionTotalNumberQuery
     * @return
     */
    List<CollectionTotalNumberVo> collectionTotalNumberExcel(CollectionTotalNumberQuery collectionTotalNumberQuery);

    /**
     * 每个用户藏品总量查询
     * @return
     */
    TableDataInfo<UserCollectionNumberVo> userCollectionNumber(UserCollectionNumberQuery userCollectionNumberQuery);

    /**
     * 每个用户藏品总量数据导出
     * @return
     */
    List<UserCollectionNumberVo> userCollectionNumberExcel(UserCollectionNumberQuery userCollectionNumberQuery);

}
