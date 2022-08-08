package com.manyun.admin.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("首页")
@Data
public class FrontPageVo {

    @ApiModelProperty("总用户")
    private Long totalUsers;

    @ApiModelProperty("当日新增用户")
    private Long nowAddUsers;

    @ApiModelProperty("近七日每日新增用户")
    private List<UserAddStatisticsVo> statisticsVoList;

    @ApiModelProperty("总订单")
    private Long totalOrders;

    @ApiModelProperty("当日新增订单")
    private Long nowAddOrders;

    @ApiModelProperty("当日成交订单")
    private Long nowCompleteOrders;

}
