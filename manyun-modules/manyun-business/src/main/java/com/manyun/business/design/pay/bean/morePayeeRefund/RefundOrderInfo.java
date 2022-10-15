package com.manyun.business.design.pay.bean.morePayeeRefund;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;


@Data
@EqualsAndHashCode
public class RefundOrderInfo {

    //退款订单号
    private String refund_seqno;
    //订单信息
    private String order_info;
    //退款订单时间
    private String refund_time;
    //退款总金额
    private BigDecimal refund_amount;

}
