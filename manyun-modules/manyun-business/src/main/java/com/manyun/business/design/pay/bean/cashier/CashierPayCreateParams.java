package com.manyun.business.design.pay.bean.cashier;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 账户+收银台 请求参数
 */
@Data
@EqualsAndHashCode
public class CashierPayCreateParams {
    private String timestamp;
    private String oid_partner;
    private String txn_type;
    private String user_id;
    private String user_type;
    private String notify_url;
    private String return_url;
    private String pay_expire;
    private String flag_chnl;
    private String risk_item;
    private String extend;

    // 商户订单信息
    private CashierPayCreateOrderInfo orderInfo;
    // 收款方信息
    private CashierPayCreatePayeeInfo[] payeeInfo;
    // 商户自定义样式参数
    private CashierPayCreateStyle style;
    // 付款方信息
    private CashierPayCreatePayerInfo payerInfo;
}
