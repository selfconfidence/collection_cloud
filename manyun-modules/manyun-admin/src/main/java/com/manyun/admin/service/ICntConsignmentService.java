package com.manyun.admin.service;

import java.util.List;

import com.manyun.admin.domain.query.ConsignmentQuery;
import com.manyun.admin.domain.vo.CntConsignmentVo;

/**
 * 寄售市场主_寄售订单Service接口
 *
 * @author yanwei
 * @date 2022-07-14
 */
public interface ICntConsignmentService
{

    /**
     * 订单管理列表
     *
     * @param consignmentQuery
     * @return 寄售市场主_寄售订单集合
     */
    public List<CntConsignmentVo> selectCntConsignmentList(ConsignmentQuery consignmentQuery);

}
