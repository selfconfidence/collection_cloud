package com.manyun.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.admin.domain.CntConsignment;
import com.manyun.admin.domain.dto.ConsignmentInfoDto;
import com.manyun.admin.domain.dto.OrderInfoDto;
import com.manyun.admin.domain.dto.PaymentReviewDto;
import com.manyun.admin.domain.query.ConsignmentQuery;
import com.manyun.admin.domain.query.ConsignmentStatusQuery;
import com.manyun.admin.domain.query.OrderListQuery;
import com.manyun.admin.domain.vo.CntConsignmentVo;
import com.manyun.admin.domain.vo.CntOrderVo;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.web.page.PageQuery;
import com.manyun.common.core.web.page.TableDataInfo;

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
}
