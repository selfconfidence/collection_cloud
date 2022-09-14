package com.manyun.business.design.pay.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户开户申请(页面接入) 请求参数
 */
@Data
@EqualsAndHashCode
public class OpenacctApplyParams {
    private String timestamp;
    private String oid_partner;
    private String user_id;
    private String txn_seqno;
    private String txn_time;
    private String flag_chnl;
    private String return_url;
    private String notify_url;
    private String user_type;

    // 开户基本信息
    private OpenacctApplyBasicInfo basicInfo;
    // 开户账户申请信息
    private OpenacctApplyAccountInfo accountInfo;
}
