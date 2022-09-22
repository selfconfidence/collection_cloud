package com.manyun.common.core.enums;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;

/**
 * 回调信息
 *
 * @author yanwei
 */
@Getter
public enum LianLianPayEnum
{

    COLLECTION_BOX_SHANDE_PAY("https://dcalliance.com.cn/prod-api/business/notify_pay/LlPay/collectionJoinBoxNotify", "盲盒藏品支付",""),
    CONSIGNMENT_SHANDE_PAY("/business/notify_pay/LlPay/consignmentNotify", "寄售支付",""),
    AUCTION_MARGIN_SHANDE_PAY("/business/notify_pay/LlPay/auctionMarginNotify", "保证金支付", ""),
    AUCTION_SHANDE_PAY("/business/notify_pay/LlPay/auctionNotify", "拍卖支付", ""),
    AUCTION_FIX_SHANDE_PAY("/business/notify_pay/LlPay/auctionFixNotify", "一口价支付", ""),
    INNER_USER("/business/notify_pay/LlPay/LlInnerUserNotify", "连连支付开户回调", ""),
    BIND_CARD_APPLY("/business/notify_pay/LlPay/LlBindCardApplyNotify", "连连支付新增绑卡申请回调", ""),
    USER_TOPUP("/business/notify_pay/LlPay/LlUserTopupNotify", "连连支付充值回调", ""),
    GENERAL_CONSUME("/business/notify_pay/LlPay/LlGeneralConsumeNotify", "连连支付消费回调", ""),
    WITHDRAW("/business/notify_pay/LlPay/LlWithdrawNotify", "连连支付提现回调", "");
    private final String notifyUrl;
    private final String body;
    private  String returnUrl;


    public LianLianPayEnum setReturnUrl(String returnUrl, String defaultReturnUrl){
        this.returnUrl = StrUtil.emptyToDefault(returnUrl, defaultReturnUrl);
        return this;
    }
    LianLianPayEnum(String notifyUrl, String body, String returnUrl)
    {
        this.notifyUrl = notifyUrl;
        this.body = body;
        this.returnUrl =returnUrl;
    }

}
