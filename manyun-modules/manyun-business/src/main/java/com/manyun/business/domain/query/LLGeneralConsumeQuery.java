package com.manyun.business.domain.query;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LLGeneralConsumeQuery {

    //订单号
    private String orderId;
    //商品名称
    private String goodsName;
    //商品金额
    private BigDecimal amount;
    //用户id
    private String userId;
    //用户真实姓名
    private String realName;
    //用户手机号
    private String phone;
    //ip
    private String ipAddr;
    //ios Android h5 表单提交之后跳转回app的地址
    private String returnUrl;

    private String notifyUrl;

}
