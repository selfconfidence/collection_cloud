package com.manyun.business.design.pay.bean.txn;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 交易二次短信验证 响应参数
 */
@Data
@EqualsAndHashCode
public class ValidationSmsResult {
    private String ret_code;
    private String ret_msg;
    private String oid_partner;
    private String user_id;
    private String txn_seqno;
    private Double total_amount;
    private String accp_txno;
    private String accounting_date;
    private String finish_time;
}
