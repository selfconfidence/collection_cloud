package com.manyun.business.design.pay.bean.morePayeeRefund;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode
public class MorePayeeRefundParams {

    //时间戳
    private String timestamp;

    //商户号
    private String oid_partner;

    //原交易付款方user_id
    private String user_id;

    //异步通知接收地址
    private String notify_url;

    //退款原因
    private String refund_reason;

    //原商户订单信息
    private OriginalOrderInfo originalOrderInfo;

    //退款订单信息
    private RefundOrderInfo refundOrderInfo;

    //原收款方退款信息
    private PyeeRefundInfos pyeeRefundInfos;

    //原付款方式退款规则信息
    private RefundMethods refundMethods;

}
