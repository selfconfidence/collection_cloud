package com.manyun.business.design.pay.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 退款结果查询响应参数
 */
@Data
@EqualsAndHashCode
@ApiModel(value = "退款结果查询响应参数")
public class QueryRefundResult {

    @ApiModelProperty("状态码")
    private String ret_code;
    @ApiModelProperty("结果描述")
    private String ret_msg;
    @ApiModelProperty("商户的唯一编号")
    private String oid_partner;
    @ApiModelProperty("账务日期")
    private String accounting_date;
    @ApiModelProperty("ACCP系统退款单号")
    private String accp_txno;
    @ApiModelProperty("渠道退款单号")
    private String chnl_txno;
    @ApiModelProperty("已退金额")
    private String refund_amount;
    @ApiModelProperty("退款交易状态   TRADE_SUCCESS：退款成功  TRADE_FAILURE：退款失败  TRADE_PROCESSING：退款处理中")
    private String txn_status;

}
