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
    //收款方用户id  用户和用户交易时候所需
    private String payeeUserId;
    //用户真实姓名
    private String realName;
    //用户手机号
    private String phone;
    //用户身份证号
    private String cartNo;
    //ip
    private String ipAddr;
    //ios Android h5 表单提交之后跳转回app的地址
    private String returnUrl;
    //回调地址
    private String notifyUrl;

    //服务费
    private BigDecimal serviceCharge;

}
