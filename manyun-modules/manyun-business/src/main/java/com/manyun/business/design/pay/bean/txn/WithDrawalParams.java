package com.manyun.business.design.pay.bean.txn;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 提现申请 请求参数
 */
@Data
@EqualsAndHashCode
public class WithDrawalParams {
    private String timestamp;
    private String oid_partner;
    private String notify_url;
    private String pay_expire;
    private String risk_item;
    // 绑定银行账号
    private String linked_acctno;
    // 绑卡协议号
    private String linked_agrtno;
    /*
    垫资标识。标识该笔提现交易是否支持平台商户垫资，适用于用户提现业务场景。
    默认：N
    Y：支持垫资
    N：不支持垫资
     */
    private String funds_flag;
    /*
    审核标识。标识该笔订单是否需要审核，默认:N
    Y:需要提现确认
    N：无需提现确认
     */
    private String check_flag;
    /*
    到账类型。
    TRANS_THIS_TIME :实时到账
    TRANS_NORMAL :普通到账（2小时内）
    TRANS_NEXT_TIME :次日到账
    默认：实时到账。
    商户订单信息orderInfo
     */
    private String pay_time_type;

    // 商户订单信息
    private WithDrawalOrderInfo orderInfo;
    // 付款方信息
    private WithDrawalPayerInfo payerInfo;
}
