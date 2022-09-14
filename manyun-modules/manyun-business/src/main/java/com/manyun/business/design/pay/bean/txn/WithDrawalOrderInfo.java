package com.manyun.business.design.pay.bean.txn;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode
public class WithDrawalOrderInfo {
    private String txn_seqno;
    private String txn_time;
    private BigDecimal total_amount;
    private BigDecimal fee_amount;
    private String order_info;
    // 交易附言。提现交易附言。单笔金额大于等于5w必须提供
    private String postscript;
}
