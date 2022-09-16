package com.manyun.business.design.pay.bean.cashier;

import lombok.Data;

@Data
public class ResponsePayer {

    private String payer_type;

    private String payer_id;

    private String method;

    private String amount;

}
