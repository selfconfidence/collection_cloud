package com.manyun.business.design.pay.bean.morePayeeRefund;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;


@Data
@EqualsAndHashCode
public class OriginalOrderInfo {

    //原支付交易商户系统唯一交易流水号
    private String txn_seqno;

    //订单总金额
    private BigDecimal total_amount;

}
