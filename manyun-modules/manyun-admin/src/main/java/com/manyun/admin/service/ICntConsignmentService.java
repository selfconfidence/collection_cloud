package com.manyun.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.admin.domain.CntConsignment;
import com.manyun.admin.domain.dto.ConsignmentInfoDto;
import com.manyun.admin.domain.dto.PaymentReviewDto;
import com.manyun.admin.domain.query.ConsignmentQuery;
import com.manyun.admin.domain.vo.CntConsignmentVo;
import com.manyun.common.core.domain.R;
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
     * 打款审核
     * @param paymentReviewDto
     * @return
     */
    R paymentReview(PaymentReviewDto paymentReviewDto);

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
}
