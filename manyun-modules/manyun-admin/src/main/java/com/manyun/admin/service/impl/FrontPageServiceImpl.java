package com.manyun.admin.service.impl;

import com.manyun.admin.domain.CntOrder;
import com.manyun.admin.domain.CntUser;
import com.manyun.admin.domain.vo.FrontPageVo;
import com.manyun.admin.domain.vo.UserAddStatisticsVo;
import com.manyun.admin.service.ICntOrderService;
import com.manyun.admin.service.ICntUserService;
import com.manyun.admin.service.IFrontPageService;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.enums.OrderStatus;
import com.manyun.common.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FrontPageServiceImpl implements IFrontPageService
{

    @Autowired
    private ICntUserService userService;

    @Autowired
    private ICntOrderService orderService;

    @Override
    public R<FrontPageVo> list() {
        //总用户数
        List<CntUser> userList = userService.list();
        //当日新增用户数
        long nowUserCount = userList.stream().filter(f ->DateUtils.compareTo(DateUtils.getNowDate(),f.getCreatedTime(),DateUtils.YYYY_MM_DD)==0).count();
        //查询近七日每日新增数
        List<UserAddStatisticsVo> statisticsVoList=userService.userAddStatistics();
        //总订单数
        List<CntOrder> orderList = orderService.list();
        //当日新增订单数
        long nowAddOrders = orderList.stream().filter(f -> DateUtils.compareTo(DateUtils.getNowDate(),f.getCreatedTime(),DateUtils.YYYY_MM_DD)==0).count();
        //当日成交订单数
        long nowCompleteOrders = orderList.stream().filter(f -> (DateUtils.compareTo(DateUtils.getNowDate(),f.getCreatedTime(),DateUtils.YYYY_MM_DD)==0 && f.getOrderStatus() == OrderStatus.OVER_ORDER.getCode())).count();
        return R.ok(Builder.of(FrontPageVo::new)
                .with(FrontPageVo::setTotalUsers,Long.valueOf(userList.size()))
                .with(FrontPageVo::setNowAddUsers,nowUserCount)
                .with(FrontPageVo::setStatisticsVoList,statisticsVoList)
                .with(FrontPageVo::setTotalOrders,Long.valueOf(orderList.size()))
                .with(FrontPageVo::setNowAddOrders,nowAddOrders)
                .with(FrontPageVo::setNowCompleteOrders,nowCompleteOrders).build());
    }

}
