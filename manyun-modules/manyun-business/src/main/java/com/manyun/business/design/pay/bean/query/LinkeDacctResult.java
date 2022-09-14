package com.manyun.business.design.pay.bean.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 绑卡信息 响应参数
 */
@Data
@EqualsAndHashCode
public class LinkeDacctResult {
    private String ret_code;
    private String ret_msg;
    /**
     * ACCP系统分配给平台商户的唯一编号。
     */
    private String oid_partner;
    /**
     * 商户用户唯一编号。用户在商户系统中的唯一编号，要求该编号在商户系统能唯一标识用户。
     */
    private String user_id;
    /**
     * 绑定银行帐号列表linked_acctlist
     */
    private List<LinkedAcctlist> linked_acctlist;
}
