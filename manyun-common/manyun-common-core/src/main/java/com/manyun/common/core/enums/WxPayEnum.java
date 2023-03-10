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
    BOX_WECHAT_PAY("/notify_pay/wechatPay/boxNotify", "盲盒支付"),
    CONSIGNMENT_WECHAT_PAY("/notify_pay/wechatPay/boxxxxNotify", "盲盒支付"),
    AUCTION_WECHAT_PAY("/notify_pay/auctionPay/auctionNotify", "拍卖支付"),
    MARGIN_WECHAT_PAY("/notify_pay/wechatPay/marginNotify", "保证金支付"),
    FIXED_WECHAT_PAY("/notify_pay/wechatPay/fixedNotify", "一口价支付");

    private final String notifyUrl;
    private final String body;

    WxPayEnum(String notifyUrl, String body)
    {
        this.notifyUrl = notifyUrl;
        this.body = body;
    }


}
