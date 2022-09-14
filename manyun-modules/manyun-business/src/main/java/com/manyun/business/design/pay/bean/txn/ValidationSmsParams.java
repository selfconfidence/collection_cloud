package com.manyun.business.design.pay.bean.txn;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 交易二次短信验证 请求参数
 */
@Data
@EqualsAndHashCode
public class ValidationSmsParams {
    private String timestamp;
    private String oid_partner;
    private String payer_type;
    private String payer_id;
    private String txn_seqno;
    private String total_amount;
    private String token;
    private String verify_code;
}
