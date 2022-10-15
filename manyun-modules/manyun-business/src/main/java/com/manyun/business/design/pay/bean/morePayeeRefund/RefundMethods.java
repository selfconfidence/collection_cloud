package com.manyun.business.design.pay.bean.morePayeeRefund;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;


@Data
@EqualsAndHashCode
public class RefundMethods {

    //付款方式
    private String method;
    //退款金额
    private BigDecimal amount;

}
