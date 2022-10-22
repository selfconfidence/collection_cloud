package com.manyun.business.design.pay.bean.closePayment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 支付单关单响应参数
 */
@Data
@EqualsAndHashCode
@ApiModel(value = "支付单关单响应参数")
public class ClosePaymentResult {

    @ApiModelProperty("状态码")
    private String ret_code;
    @ApiModelProperty("结果描述")
    private String ret_msg;
    @ApiModelProperty("平台商户的唯一编号")
    private String oid_partner;
    @ApiModelProperty("商户用户唯一编号")
    private String user_id;
    @ApiModelProperty("商户系统唯一交易流水号")
    private String txn_seqno;
    @ApiModelProperty("ACCP系统交易单号")
    private String accp_txno;
    @ApiModelProperty("订单总金额")
    private String total_amount;

}
