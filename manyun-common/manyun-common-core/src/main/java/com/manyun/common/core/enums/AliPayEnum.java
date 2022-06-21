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
    BOX_PAY("/web/aliPay/test_url", "盲盒支付");

    private final String notifyUrl;
    private final String body;

    AliPayEnum(String notifyUrl, String body)
    {
        this.notifyUrl = notifyUrl;
        this.body = body;
    }


}
