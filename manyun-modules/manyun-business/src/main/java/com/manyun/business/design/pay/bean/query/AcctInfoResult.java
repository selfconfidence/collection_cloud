package com.manyun.business.design.pay.bean.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 资金流水详情列表查询 响应参数
 */
@Data
@EqualsAndHashCode
public class AcctInfoResult {
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
     * ACCP系统用户编号。用户注册成功后ACCP系统返回的用户编号。
     */
    private String oid_userno;
    /**
     * ACCP合作银行账户
     */
    private String bank_account;
    /**
     * 账户列表acctinfo_list
     */
    private List<AcctinfoList> acctinfo_list;
}
