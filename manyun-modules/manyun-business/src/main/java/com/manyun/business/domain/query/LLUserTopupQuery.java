package com.manyun.business.domain.query;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LLUserTopupQuery {

    //用户id
    private String userId;
    //用户真实姓名
    private String realName;
    //手机号
    private String phone;
    //ip
    private String ipAddr;
    //充值金额
    private BigDecimal amount;
    //ios Android h5 表单提交之后跳转回app的地址
    private String returnUrl;

}
