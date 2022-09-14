package com.manyun.business.design.pay.bean.individual;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 个人用户新增绑卡验证 请求参数
 */
@Data
@EqualsAndHashCode
public class IndividualBindCardVerifyParams {
    private String timestamp;
    private String oid_partner;
    private String user_id;
    private String txn_seqno;
    private String token;
    private String verify_code;
}
