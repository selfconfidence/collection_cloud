package com.manyun.common.core.enums;

import lombok.Getter;

/**
 * 回调信息
 *
 * @author ruoyi
 */
@Getter
public enum WxPayEnum
{
    BOX_ORDER_TEST("/web/aliPay/test_url", "盲盒支付");

    private final String notifyUrl;
    private final String body;

    WxPayEnum(String notifyUrl, String body)
    {
        this.notifyUrl = notifyUrl;
        this.body = body;
    }


}
