package com.manyun.business.design.pay.bean.cashier;

import lombok.Data;

@Data
public class ResponsePayee {

    private String payer_type;

    private String payer_id;

    private String amount;

}
