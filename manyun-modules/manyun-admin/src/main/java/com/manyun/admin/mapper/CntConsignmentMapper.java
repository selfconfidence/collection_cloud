package com.manyun.admin.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.admin.domain.CntConsignment;
import com.manyun.admin.domain.dto.ConsignmentInfoDto;
import com.manyun.admin.domain.query.CollectionBusinessQuery;
import com.manyun.admin.domain.query.CollectionSalesStatisticsQuery;
import com.manyun.admin.domain.query.ConsignmentQuery;
import com.manyun.admin.domain.vo.CntConsignmentVo;
import com.manyun.admin.domain.vo.CollectionBusinessVo;
import com.manyun.admin.domain.vo.CollectionSalesStatisticsVo;

/**
 * 寄售市场主_寄售订单Mapper接口
 *
 * @author yanwei
 * @date 2022-07-14
 */
public interface CntConsignmentMapper extends BaseMapper<CntConsignment>
{

    /***
     * 查询藏品订单管理列表
     * @param consignmentQuery
     * @return
     */
    List<CntConsignmentVo> selectCollectionOrderList(ConsignmentQuery consignmentQuery);

    /***
     * 查询盲盒订单管理列表
     * @param consignmentQuery
     * @return
     */
    List<CntConsignmentVo> selectBoxOrderList(ConsignmentQuery consignmentQuery);

    /**
     * 获取藏品订单管理详细信息
     * @param consignmentInfoDto
     * @return
     */
    CntConsignmentVo selectConsignmentOrderById(ConsignmentInfoDto consignmentInfoDto);

    /**
     * 规定时间藏品交易量及交易金额查询
     * @param collectionBusinessQuery
     * @return
     */
    List<CollectionBusinessVo> selectByTimeZones(CollectionBusinessQuery collectionBusinessQuery);

    /**
     * 规定时间指定藏品买卖查询
     * @param collectionSalesStatisticsQuery
     * @return
     */
    List<CollectionSalesStatisticsVo> selectCollectionSalesStatistics(CollectionSalesStatisticsQuery collectionSalesStatisticsQuery);
}
