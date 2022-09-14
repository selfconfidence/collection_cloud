package com.manyun.business.design.pay.bean.cashier;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 账户+收银台 响应参数
 */
@Data
@EqualsAndHashCode
public class CashierPayCreateResult {
    private String ret_code;
    private String ret_msg;
    private String oid_partner;
    private String user_id;
    private Double total_amount;
    private String txn_seqno;
    private String accp_txno;
    private String gateway_url;
}
