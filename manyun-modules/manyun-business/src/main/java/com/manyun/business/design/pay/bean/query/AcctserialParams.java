package com.manyun.business.design.pay.bean.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 资金流水详情列表查询 请求参数
 */
@Data
@EqualsAndHashCode
public class AcctserialParams {
    private String timestamp;
    private String oid_partner;
    private String user_id;
    private String user_type;
    private String acct_type;
    private String date_start;
    private String date_end;
    private String flag_dc;
    private String page_no;
    private String page_size;
    private String sort_type;
}
