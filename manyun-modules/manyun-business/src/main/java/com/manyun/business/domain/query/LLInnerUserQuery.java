package com.manyun.business.domain.query;

import lombok.Data;

@Data
public class LLInnerUserQuery {

    //用户id
    private String userId;
    //真实姓名
    private String realName;
    //手机号
    private String phone;
    //身份证号
    private String cartNo;
    //ios Android h5 表单提交之后跳转回app的地址
    private String returnUrl;
    //回调地址
    private String notifyurl;
}
