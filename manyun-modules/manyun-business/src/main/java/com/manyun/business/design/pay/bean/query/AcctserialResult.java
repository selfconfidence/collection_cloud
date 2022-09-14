package com.manyun.business.design.pay.bean.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 资金流水详情列表查询 响应参数
 */
@Data
@EqualsAndHashCode
public class AcctserialResult {
    private String ret_code;
    private String ret_msg;
    //ACCP系统分配给平台商户的唯一编号。
    private String oid_partner;
    //商户用户唯一编号。用户在商户系统中的唯一编号，要求该编号在商户系统能唯一标识用户。
    private String user_id;
    //当前页码，表示返回结果集第几页。
    private String page_no;
    //出账总金额，表示当前查询条件下的出账总金额，单位：元。
    private String total_out_amt;
    //入账总金额，表示当前查询条件下的入账总金额，单位：元。
    private String total_in_amt;
    //结果集总数，表示当前查询条件下的结果集数据总数。
    private String total_num;
    //结果集总页数，total_page=(total_num/page_size) 向上取整。
    private String total_page;
    //资金流水列表acctbal_list
    private List<AcctserialAcctbal> acctbal_list;
}
