package com.manyun.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.admin.domain.CntConsignment;
import com.manyun.admin.domain.dto.ConsignmentInfoDto;
import com.manyun.admin.domain.dto.OrderInfoDto;
import com.manyun.admin.domain.dto.PaymentReviewDto;
import com.manyun.admin.domain.query.*;
import com.manyun.admin.domain.vo.CntConsignmentVo;
import com.manyun.admin.domain.vo.CntOrderVo;
import com.manyun.admin.domain.vo.CollectionBusinessVo;
import com.manyun.admin.domain.vo.CollectionSalesStatisticsVo;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.web.page.PageQuery;
import com.manyun.common.core.web.page.TableDataInfo;

import java.util.List;

/**
 * 寄售市场主_寄售订单Service接口
 *
 * @author yanwei
 * @date 2022-07-14
 */
public interface ICntConsignmentService extends IService<CntConsignment>
{

    /**
     * 订单管理列表
     *
     */
    TableDataInfo<CntOrderVo> orderList(OrderListQuery orderListQuery);

    /**
     * 订单列表详情
     *
     */
    R<CntOrderVo> orderInfo(OrderInfoDto orderInfoDto);

    /**
     * 藏品订单管理列表
     *
     * @param consignmentQuery
     * @return 寄售市场主_寄售订单集合
     */
    TableDataInfo<CntConsignmentVo> collectionList(ConsignmentQuery consignmentQuery);

    /**
     * 盲盒订单管理列表
     *
     * @param consignmentQuery
     * @return 寄售市场主_寄售订单集合
     */
    TableDataInfo<CntConsignmentVo> boxList(ConsignmentQuery consignmentQuery);

    /**
     * 获取藏品订单管理详细信息
     * @param consignmentInfoDto
     * @return
     */
    CntConsignmentVo selectConsignmentOrderById(ConsignmentInfoDto consignmentInfoDto);

    /**
     * 修改寄售状态
     * @param consignmentStatusQuery
     * @return
     */
    int consignmentStatus(ConsignmentStatusQuery consignmentStatusQuery);

    /**
     *规定时间藏品交易量及交易金额查询
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
