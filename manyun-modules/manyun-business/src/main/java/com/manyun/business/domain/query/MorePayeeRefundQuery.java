package com.manyun.business.domain.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


@Data
@ApiModel(value = "退款申请请求参数")
public class MorePayeeRefundQuery {

    @ApiModelProperty(value = "原交易付款方user_id，用户在商户系统中的唯一编号",required = true)
    @NotBlank(message = "原交易付款方user_id不能为空!")
    private String userId;

    @ApiModelProperty(value = "原支付交易商户系统唯一交易流水号",required = true)
    @NotBlank(message = "原支付交易商户系统唯一交易流水号不能为空!")
    private String txn_seqno;

    @ApiModelProperty(value = "订单总金额",required = true)
    @NotNull(message = "订单总金额不能为空!")
    private BigDecimal total_amount;

    @ApiModelProperty(value = "付款方式",required = true)
    @NotBlank(message = "付款方式不能为空!")
    private String method;

}
