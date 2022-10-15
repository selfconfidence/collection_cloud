package com.manyun.business.design.pay.bean.queryPayment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@ApiModel(value = "交易查询结果响应参数")
public class QueryPaymentResult {

    @ApiModelProperty("状态码")
    private String ret_code;

    @ApiModelProperty("结果描述")
    private String ret_msg;

    @ApiModelProperty("商户的唯一编号")
    private String oid_partner;

    @ApiModelProperty("交易类型  用户充值：USER_TOPUP   商户充值：MCH_TOPUP    普通消费：GENERAL_CONSUME    担保消费：SECURED_CONSUME    担保确认：SECURED_CONFIRM   内部代发：INNER_FUND_EXCHANGE    外部代发：OUTER_FUND_EXCHANGE")
    private String txn_type;

    @ApiModelProperty("账务日期")
    private String accounting_date;

    @ApiModelProperty("支付完成时间")
    private String finish_time;

    @ApiModelProperty("ACCP系统交易单号")
    private String accp_txno;

    @ApiModelProperty("渠道交易单号")
    private String chnl_txno;

    @ApiModelProperty("支付交易状态 TRADE_WAIT_PAY:交易处理中   TRADE_SUCCESS:交易成功  TRADE_CLOSE:交易失败")
    private String txn_status;

    @ApiModelProperty("商户订单信息")
    private OrderInfo orderInfo;

    @ApiModelProperty("付款方信息")
    private PayerInfo[] payerInfo;

    @ApiModelProperty("收款方信息")
    private PayeeInfo[] payeeInfo;

}
