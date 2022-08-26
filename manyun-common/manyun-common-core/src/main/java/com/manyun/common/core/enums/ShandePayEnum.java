package com.manyun.common.core.enums;

import lombok.Getter;

/**
 * 回调信息
 *
 * @author yanwei
 */
@Getter
public enum ShandePayEnum
{
    BOX_SHANDE_PAY("/business/notify_pay/ShandePay/ShandeNotify", "盲盒支付");

    private final String notifyUrl;
    private final String body;

    ShandePayEnum(String notifyUrl, String body)
    {
        this.notifyUrl = notifyUrl;
        this.body = body;
    }


}
