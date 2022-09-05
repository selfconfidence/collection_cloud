package com.manyun.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.admin.domain.CntOrder;
import com.manyun.admin.domain.query.OrderListQuery;
import com.manyun.admin.domain.vo.CntOrderVo;
import com.manyun.admin.domain.vo.OrderAmountsAddStatisticsVo;
import com.manyun.admin.domain.vo.OrderTypePercentageVo;
import com.manyun.admin.domain.vo.ValidOrderAddStatisticsVo;

import java.util.List;

/**
 * 订单Service接口
 *
 * @author yanwei
 * @date 2022-07-26
 */
public interface ICntOrderService extends IService<CntOrder>
{

    /**
     * 我的订单
     */
    List<CntOrderVo> myOrderList(String userId);

    /**
     *查询近七日每日新增有效订单数
     */
    List<ValidOrderAddStatisticsVo> validOrderAddStatistics();

    /**
     *查询近期日每日新增销售金额
     */
    List<OrderAmountsAddStatisticsVo> orderAmountsAddStatistics();

    /**
     *查询订单各种状态占订单总量比例
     */
    List<OrderTypePercentageVo> orderTypePercentageList();

    /**
     * 订单列表
     * @return
     */
    List<CntOrderVo> orderList(OrderListQuery orderListQuery);
}
