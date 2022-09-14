package com.manyun.business.design.pay.bean.cashier;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class CashierPayCreatePayerInfo {
    private String payer_type;
    private String payer_id;
}
