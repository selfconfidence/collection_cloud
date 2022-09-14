package com.manyun.business.design.pay.bean.cashier;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode
public class CashierPayCreatePayeeInfo {
    private String payee_id;
    private String payee_type;
    private String payee_accttype;
    private BigDecimal payee_amount;
    private String payee_memo;
}
