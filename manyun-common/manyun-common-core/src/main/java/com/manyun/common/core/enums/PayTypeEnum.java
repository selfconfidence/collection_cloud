package com.manyun.common.core.enums;

import lombok.Getter;

/**
 * 支付类型信息
 *
 * @author yanwei
 *
 * 0=平台 1=微信,2=支付宝,3=银联,4=杉德
 */
@Getter
public enum PayTypeEnum
{
    WECHAT_TYPE(Integer.valueOf(1), "微信"), ALI_TYPE(2, "支付宝"), MONEY_TAPE(0, "余额"), CART_TAPE(3, "银联"), SHANDE_TYPE(4, "杉德"), LIANLIAN_TYPE(5, "连连支付");

    private final Integer code;
    private final String info;

    PayTypeEnum(Integer code, String info)
    {
        this.code = code;
        this.info = info;
    }

}
