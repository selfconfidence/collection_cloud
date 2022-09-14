package com.manyun.business.design.pay.bean.individual;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 个人用户新增绑卡验证 响应参数
 */
@Data
@EqualsAndHashCode
public class IndividualBindCardVerifyResult {
    private String ret_code;
    private String ret_msg;
    private String oid_partner;
    private String user_id;
    private String txn_seqno;
    private String accp_txno;
    private String linked_agrtno;
}
