package com.manyun.common.core.enums;

import lombok.Getter;

/**
 * 验证码类型
 * 0=登录验证码，1=实名验证码，2=修改登录密码，3=修改支付密码
 */

@Getter
public enum PhoneCodeType {

    LOGIN_CODE("login_code", "登录验证码"), REAL_CODE("real_code", "实名验证码"),REG_CODE("reg_code", "注册验证码"),
    CHANGE_LOGIN_PASS("change_login_pass", "修改登录密码"), CHANGE_PAY_PASS("change_pay_pass", "修改支付密码");

    private final String type;
    private final String info;

    PhoneCodeType(String type, String info)
    {
        this.type = type;
        this.info = info;
    }

}
