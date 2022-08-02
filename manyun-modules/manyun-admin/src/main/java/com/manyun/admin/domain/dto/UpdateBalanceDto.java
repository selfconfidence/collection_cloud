package com.manyun.admin.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("修改余额求情参数")
public class UpdateBalanceDto {

    @ApiModelProperty("用户id;平台内部生成,短编号")
    private String userId;

    @ApiModelProperty("钱包余量")
    private BigDecimal moneyBalance;

}
