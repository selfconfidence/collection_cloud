package com.manyun.admin.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.admin.domain.CntOrder;
import com.manyun.admin.domain.vo.CntOrderVo;
import com.manyun.admin.domain.vo.OrderAmountsAddStatisticsVo;
import com.manyun.admin.domain.vo.OrderTypePercentageVo;
import com.manyun.admin.domain.vo.ValidOrderAddStatisticsVo;

/**
 * 订单Mapper接口
 *
 * @author yanwei
 * @date 2022-07-13
 */
public interface CntOrderMapper extends BaseMapper<CntOrder>
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
}
