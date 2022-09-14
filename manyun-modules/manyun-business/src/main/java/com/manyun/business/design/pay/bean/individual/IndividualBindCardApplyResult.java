package com.manyun.business.design.pay.bean.individual;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 个人用户开户验证 响应参数
 */
@Data
@EqualsAndHashCode
public class IndividualBindCardApplyResult {
    private String ret_code;
    private String ret_msg;
    private String oid_partner;
    private String user_id;
    private String txn_seqno;
    private String accp_txno;
    private String token;
}
