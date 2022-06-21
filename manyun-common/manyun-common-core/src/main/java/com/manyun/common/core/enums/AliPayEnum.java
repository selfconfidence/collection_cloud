package com.manyun.common.core.enums;

import lombok.Getter;

/**
 * 回调信息
 *
 * @author yanwei
 */
@Getter
public enum AliPayEnum
{
    BOX_ALI_PAY("/business/notify_pay/aliPay/boxNotify", "盲盒支付");

    private final String notifyUrl;
    private final String body;

    AliPayEnum(String notifyUrl, String body)
    {
        this.notifyUrl = notifyUrl;
        this.body = body;
    }


}
