package com.manyun.business.design.pay.bean.individual;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 个人用户新增绑卡 请求参数
 */
@Data
@EqualsAndHashCode
public class IndividualBindCardApplyParams {
    private String timestamp;
    private String oid_partner;
    private String user_id;
    private String txn_seqno;
    private String txn_time;
    private String notify_url;
    private String risk_item;
    private String linked_acctno;
    private String linked_phone;
    private String password;
    private String random_key;
}
