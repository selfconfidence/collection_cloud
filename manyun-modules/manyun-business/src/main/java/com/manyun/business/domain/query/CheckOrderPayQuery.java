package com.manyun.business.domain.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("支付结果查询相关条件")
public class CheckOrderPayQuery {

    @ApiModelProperty("订单号")
    @NotBlank(message = "订单号不能为空")
    private String orderNo;

    @ApiModelProperty("类型，从哪里来的 1，正常购买，2保证金，3一口价，4拍卖支付")
    private Integer type;
}
