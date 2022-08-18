package com.manyun.admin.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@ApiModel("首页")
@Data
public class FrontPageVo {

    @ApiModelProperty("总会员数量")
    private Long totalUsers;

    @ApiModelProperty("用户藏品持有总量")
    private Long totalUserCollections;

    @ApiModelProperty("今日销售额")
    private BigDecimal totalAmounts;

    @ApiModelProperty("有效订单数量")
    private Long totalValidOrders;

    @ApiModelProperty("近七日每日新增用户")
    private List<UserAddStatisticsVo> userAddStatisticsVoList;

    @ApiModelProperty("近七日每日新增有效订单数")
    private List<ValidOrderAddStatisticsVo> validOrderAddStatisticsVoList;

    @ApiModelProperty("近七日每日新增销售金额")
    private List<OrderAmountsAddStatisticsVo> orderAmountsAddStatisticsVoList;

    @ApiModelProperty("订单各种状态占订单总量比例")
    private OrderTypeNumberVo orderTypeNumberVo;

}
