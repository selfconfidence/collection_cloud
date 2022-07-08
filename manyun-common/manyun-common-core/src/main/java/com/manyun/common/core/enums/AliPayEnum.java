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
    BOX_ALI_PAY("/business/notify_pay/aliPay/boxNotify", "盲盒支付"),
    CONSIGNMENT_ALI_PAY("/business/notify_pay/aliPay/boxxxxNotify", "盲盒支付"),
    AUCTION_ALI_PAY("/business/notify_pay/alipay/auctionNotify", "拍卖支付"),
    MARGIN_ALI_PAY("/business/notify_pay/alipay/marginNotify", "保证金支付"),
    FIXED_ALI_PAY("/business/notify_pay/alipay/fixedNotify", "一口价支付");

    private final String notifyUrl;
    private final String body;

    AliPayEnum(String notifyUrl, String body)
    {
        this.notifyUrl = notifyUrl;
        this.body = body;
    }


}
