package com.manyun.business.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("连连余额查询")
public class LlBalanceVo {

    @ApiModelProperty("总余额")
    private BigDecimal totalBalance;

    @ApiModelProperty("可提现余额")
    private BigDecimal withdrawBalance;

    @ApiModelProperty("待结算余额")
    private BigDecimal psettleBalance;

}
