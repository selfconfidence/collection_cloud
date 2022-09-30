package com.manyun.business.design.pay.bean.queryPayment;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode
public class QueryPaymentParams {

    //时间戳
    private String timestamp;

    //商户号
    private String oid_partner;

    //交易流水
    private String txn_seqno;

    //ACCP系统交易单号
    private String accp_txno;

    //上游渠道流水号
    private String sub_chnl_no;

}
