package com.manyun.business.design.pay.bean.individual;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 个人用户解绑银行卡 请求参数
 */
@Data
@EqualsAndHashCode
public class UnlinkedacctIndApplyParams {
    private String timestamp;
    private String oid_partner;
    private String user_id;
    private String txn_seqno;
    private String txn_time;
    private String notify_url;
    private String linked_acctno;
    private String password;
    private String random_key;
}
