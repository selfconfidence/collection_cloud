package com.manyun.common.core.enums;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;

@Getter
public enum  SandAccountEnum {

    OPEN_ACCOUNT("/business/notify_pay/sandWallet/sandCreateAccountNotify", "杉德云账户开户回调", ""),
    SAND_ACCOUNT_PAY("/business/notify_pay/sandWallet/sandCreateAccountNotify", "杉德云账户支付回调", ""),

    COLLECTION_BOX_SANDACCOUNT_PAY("/business/notify_pay/sandWallet/collectionJoinBoxNotify", "盲盒藏品支付",""),
    CONSIGNMENT_SANDACCOUNT_PAY("/business/notify_pay/sandWallet/consignmentNotify", "寄售支付",""),
    AUCTION_MARGIN_SANDACCOUNT_PAY("/business/notify_pay/sandWallet/auctionMarginNotify", "保证金支付", ""),
    AUCTION_SANDACCOUNT_PAY("/business/notify_pay/sandWallet/auctionNotify", "拍卖支付", ""),
    AUCTION_FIX_SANDACCOUNT_PAY("/business/notify_pay/sandWallet/auctionFixNotify", "一口价支付", "");


    private final String notifyUrl;
    private final String body;
    private  String returnUrl;


    public SandAccountEnum setReturnUrl(String returnUrl, String defaultReturnUrl){
        this.returnUrl = StrUtil.emptyToDefault(returnUrl, defaultReturnUrl);
        return this;
    }
    SandAccountEnum(String notifyUrl, String body, String returnUrl)
    {
        this.notifyUrl = notifyUrl;
        this.body = body;
        this.returnUrl =returnUrl;
    }
}
