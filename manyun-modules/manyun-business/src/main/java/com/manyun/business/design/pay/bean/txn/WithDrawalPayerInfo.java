package com.manyun.business.design.pay.bean.txn;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class WithDrawalPayerInfo {
    private String payer_type;
    private String payer_id;
    private String payer_accttype;
    private String password;
    private String random_key;
    private String pap_agree_no;
}
