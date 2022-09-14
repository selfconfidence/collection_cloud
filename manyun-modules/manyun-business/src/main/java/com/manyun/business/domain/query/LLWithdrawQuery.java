package com.manyun.business.domain.query;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LLWithdrawQuery {

    //用户Id
    private String userId;
    //支付密码
    private String passWord;
    //提现金额
    private BigDecimal amount;

}
