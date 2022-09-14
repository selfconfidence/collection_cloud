package com.manyun.business.design.pay.bean.cashier;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode
public class CashierPayCreateOrderInfo {
    private String txn_seqno;
    private String txn_time;
    private BigDecimal total_amount;
    private String order_info;
    private String goods_name;
    private String goods_url;
}
