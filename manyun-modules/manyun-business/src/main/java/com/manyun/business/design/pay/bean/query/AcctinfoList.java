package com.manyun.business.design.pay.bean.query;

import lombok.Data;

@Data
public class AcctinfoList {

    /**
     * 账户号。
     */
    private String oid_acctno;
    /**
     * 账户状态。
     * NORMAL :正常
     * CANCEL :注销
     * LOCK :锁定
     * LOSS :挂失
     * ACTIVATE_PENDING :待激活
     */
    private String acct_state;
    /**
     * acct_type
     * USEROWN_PSETTLE	用户自有待结算账户
     * USEROWN_AVAILABLE	用户自有可用账户
     * MCHOWN_PSETTLE	平台商户自有待结算账户
     * MCHOWN_AVAILABLE	平台商户自有可用账户
     * MCHASSURE_PSETTLE	平台商户担保待结算账户
     * MCHASSURE_AVAILABLE	平台商户担保可用账户
     * MCHCOUPON_PSETTLE	平台商户优惠券待结算账户
     * MCHCOUPON_AVAILABLE	平台商户优惠券可用账户
     * MCHFEE_PSETTLE	平台商户手续费结算账户
     * MCHFEE_AVAILABLE	平台商户手续费可用账户
     * BANKCARD_DEBIT	银行账户（借记卡）
     * BANKCARD_CREDIT	银行卡账户（信用卡）
     * BANKCARD_ENTERPRISE	银行账户（对公）
     * THIRD_PARTY	第三方账户
     */
    private String acct_type;
    /**
     * 资金余额。单位 元
     */
    private String amt_balcur;
    /**
     * 可用余额。单位 元
     */
    private String amt_balaval;
    /**
     * 冻结金额。单位 元
     */
    private String amt_balfrz;
    /**
     * 昨日资金余额。单位 元
     */
    private String amt_lastbal;
    /**
     * 昨日可用余额。单位 元
     */
    private String amt_lastaval;
    /**
     * 昨日冻结金额。单位 元
     */
    private String amt_lastfrz;

}
