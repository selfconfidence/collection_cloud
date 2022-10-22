package com.manyun.business.design.pay.bean.closePayment;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 支付单关单请求参数
 */
@Data
@EqualsAndHashCode
public class ClosePaymentParams {

    //时间戳
    private String timestamp;
    //商户号
    private String oid_partner;
    //商户系统唯一交易流水号
    private String txn_seqno;
    //ACCP系统交易单号
    private String accp_txno;

}
