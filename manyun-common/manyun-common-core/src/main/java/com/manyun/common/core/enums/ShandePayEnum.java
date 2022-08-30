package com.manyun.common.core.enums;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;

/**
 * 回调信息
 *
 * @author yanwei
 */
@Getter
public enum ShandePayEnum
{
    //BOX_SHANDE_PAY("https://dcalliance.com.cn/prod-api/business/notify_pay/ShandePay/ShandeNotify", "盲盒支付",""),    BOX_SHANDE_PAY("https://dcalliance.com.cn/prod-api/business/notify_pay/ShandePay/ShandeNotify", "盲盒支付",""),
   // BOX_SHANDE_PAY("https://dcalliance.com.cn/prod-api/business/notify_pay/ShandePay/boxNotify", "盲盒支付",""),

    COLLECTION_BOX_SHANDE_PAY("https://dcalliance.com.cn/prod-api/business/notify_pay/ShandePay/collectionJoinBoxNotify", "盲盒藏品支付",""),
    CONSIGNMENT_SHANDE_PAY("https://dcalliance.com.cn/prod-api/business/notify_pay/ShandePay/consignmentNotify", "寄售支付","");
    private final String notifyUrl;
    private final String body;
    private  String returnUrl;


    public ShandePayEnum setReturnUrl(String returnUrl,String defaultReturnUrl){
        this.returnUrl = StrUtil.emptyToDefault(returnUrl, defaultReturnUrl);
        return this;
    }
    ShandePayEnum(String notifyUrl, String body,String returnUrl)
    {
        this.notifyUrl = notifyUrl;
        this.body = body;
        this.returnUrl =returnUrl;
    }


}
