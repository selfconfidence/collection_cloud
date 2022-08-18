package com.manyun.admin.service.impl;

import com.manyun.admin.domain.CntOrder;
import com.manyun.admin.domain.CntUser;
import com.manyun.admin.domain.CntUserCollection;
import com.manyun.admin.domain.vo.*;
import com.manyun.admin.service.ICntOrderService;
import com.manyun.admin.service.ICntUserCollectionService;
import com.manyun.admin.service.ICntUserService;
import com.manyun.admin.service.IFrontPageService;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.enums.OrderStatus;
import com.manyun.common.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class FrontPageServiceImpl implements IFrontPageService
{

    @Autowired
    private ICntUserService userService;

    @Autowired
    private ICntUserCollectionService userCollectionService;

    @Autowired
    private ICntOrderService orderService;

    @Override
    public R<FrontPageVo> list() {
        List<CntUser> userList = userService.list();
        List<CntUserCollection> userCollectionList = userCollectionService.list();
        List<CntOrder> orderList = orderService.list();
        //总会员数量
        Integer totalUsers = userList.size();
        //用户持有藏品数量
        Integer totalUserCollections = userCollectionList.size();
        //今日销售额
        BigDecimal totalAmounts = orderList
                .stream()
                .filter(f -> (DateUtils.compareTo(DateUtils.getNowDate(), f.getCreatedTime(), DateUtils.YYYY_MM_DD) == 0 && f.getOrderStatus() == OrderStatus.OVER_ORDER.getCode()))
                .map(CntOrder::getOrderAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        //有效订单数量
        long totalValidOrders = orderList
                .stream()
                .filter(f -> f.getOrderStatus() == OrderStatus.OVER_ORDER.getCode())
                .count();
        //查询近七日每日新增会员数
        List<UserAddStatisticsVo> userAddStatisticsVoList=userService.userAddStatistics();
        //查询近七日每日新增有效订单数
        List<ValidOrderAddStatisticsVo> validOrderAddStatisticsVoList=orderService.validOrderAddStatistics();
        //查询近七日每日新增销售金额
        List<OrderAmountsAddStatisticsVo> orderAmountsAddStatisticsVoList=orderService.orderAmountsAddStatistics();
        //查询订单各种状态占订单总量比例
        OrderTypeNumberVo orderTypeNumberVo = Builder
                .of(OrderTypeNumberVo::new)
                .with(OrderTypeNumberVo::setTotalOrders, orderList.size())
                .with(OrderTypeNumberVo::setOrderTypePercentageList, orderService.orderTypePercentageList()).build();
        return R.ok(
                Builder
                .of(FrontPageVo::new)
                .with(FrontPageVo::setTotalUsers,Long.valueOf(totalUsers))
                .with(FrontPageVo::setTotalUserCollections,Long.valueOf(totalUserCollections))
                .with(FrontPageVo::setTotalAmounts,totalAmounts)
                .with(FrontPageVo::setTotalValidOrders,totalValidOrders)
                .with(FrontPageVo::setUserAddStatisticsVoList,userAddStatisticsVoList)
                .with(FrontPageVo::setValidOrderAddStatisticsVoList,validOrderAddStatisticsVoList)
                .with(FrontPageVo::setOrderAmountsAddStatisticsVoList,orderAmountsAddStatisticsVoList)
                .with(FrontPageVo::setOrderTypeNumberVo,orderTypeNumberVo)
                .build());
    }

}
