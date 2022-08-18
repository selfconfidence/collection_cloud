package com.manyun.admin.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@ApiModel("销售金额近七日新增数量统计返回视图")
@Data
public class OrderAmountsAddStatisticsVo {

    @ApiModelProperty("日期")
    private String date;

    @ApiModelProperty("每日销售总金额")
    private BigDecimal orderAmounts;

}
