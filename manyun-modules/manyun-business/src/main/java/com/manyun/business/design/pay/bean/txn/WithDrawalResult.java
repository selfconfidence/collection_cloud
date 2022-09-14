package com.manyun.business.design.pay.bean.txn;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 提现申请 响应参数
 */
@Data
@EqualsAndHashCode
public class WithDrawalResult {
    private String ret_code;
    private String ret_msg;
    private String oid_partner;
    private String user_id;
    private String txn_seqno;
    private Double total_amount;
    private Double fee_amount;
    private String accp_txno;
    private String token;
}
