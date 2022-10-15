package com.manyun.business.design.pay.bean.morePayeeRefund;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;


@Data
@EqualsAndHashCode
public class PyeeRefundInfos {

    //原收款方id
    private String payee_id;
    //原收款方类型    用户：USER     平台商户：MERCHANT
    private String payee_type;
    //原收款方账户类型  用户账户：USEROWN    平台商户自有资金账户：MCHOWN   平台商户担保账户：MCHASSURE  平台商户优惠券账户：MCHCOUPON     平台商户手续费账户：MCHFEE
    private String payee_accttype;
    //退款金额
    private BigDecimal payee_refund_amount;
    //垫资标识。当原收款方金额不足时，是否由平台垫资的标识，默认:N   Y:垫资    N：不垫资
    private String is_advance_pay;

}
