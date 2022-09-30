package com.manyun.business.design.pay.bean.queryPayment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@ApiModel(value = "交易查询结果商户订单信息响应参数")
public class OrderInfo {

    @ApiModelProperty("商户系统唯一交易流水号")
    private String txn_seqno;

    @ApiModelProperty("商户系统交易时间")
    private String txn_time;

    @ApiModelProperty("订单总金额")
    private String total_amount;

    @ApiModelProperty("用于订单说明，透传返回。")
    private String order_info;

}
