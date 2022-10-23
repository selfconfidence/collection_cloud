package com.manyun.admin.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.admin.domain.dto.OrderInfoDto;
import com.manyun.admin.domain.query.OrderListQuery;
import com.manyun.admin.domain.vo.CntOrderVo;
import com.manyun.admin.domain.vo.OrderAmountsAddStatisticsVo;
import com.manyun.admin.domain.vo.OrderTypePercentageVo;
import com.manyun.admin.domain.vo.ValidOrderAddStatisticsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manyun.admin.mapper.CntOrderMapper;
import com.manyun.admin.domain.CntOrder;
import com.manyun.admin.service.ICntOrderService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.manyun.common.core.enums.OrderStatus.CANCEL_ORDER;
import static com.manyun.common.core.enums.OrderStatus.WAIT_ORDER;

/**
 * 订单Service业务层处理
 *
 * @author yanwei
 * @date 2022-07-26
 */
@Service
public class CntOrderServiceImpl extends ServiceImpl<CntOrderMapper,CntOrder> implements ICntOrderService
{
    @Autowired
    private CntOrderMapper cntOrderMapper;

    /**
     * 我的订单
     */
    @Override
    public List<CntOrderVo> myOrderList(String userId) {
        return cntOrderMapper.myOrderList(userId);
    }

    /**
     *查询近七日每日新增有效订单数
     */
    @Override
    public List<ValidOrderAddStatisticsVo> validOrderAddStatistics() {
        return cntOrderMapper.validOrderAddStatistics();
    }

    /**
     *查询近期日每日新增销售金额
     */
    @Override
    public List<OrderAmountsAddStatisticsVo> orderAmountsAddStatistics() {
        return cntOrderMapper.orderAmountsAddStatistics();
    }

    /**
     *查询订单各种状态占订单总量比例
     */
    @Override
    public List<OrderTypePercentageVo> orderTypePercentageList() {
        return cntOrderMapper.orderTypePercentageList();
    }

    /**
     * 订单列表
     * @return
     */
    @Override
    public List<CntOrderVo> orderList(OrderListQuery orderListQuery) {
        return cntOrderMapper.orderList(orderListQuery);
    }

    /**
     * 订单列表详情
     *
     */
    @Override
    public CntOrderVo orderInfo(OrderInfoDto orderInfoDto) {
        return cntOrderMapper.orderInfo(orderInfoDto);
    }
}
