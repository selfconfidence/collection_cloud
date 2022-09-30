package com.manyun.business.design.pay.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 退款结果查询请求参数
 */
@Data
@EqualsAndHashCode
public class QueryRefundParams {

    private String timestamp;
    private String oid_partner;
    private String refund_seqno;
    private String accp_txno;

}
