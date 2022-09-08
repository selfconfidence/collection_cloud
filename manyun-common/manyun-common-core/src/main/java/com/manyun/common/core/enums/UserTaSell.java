package com.manyun.common.core.enums;

import lombok.Getter;

/**
 * 是否购买，1=未购买,2=已购买
 * 
 * @author yanwei
 *
 *是否购买，1=未购买,2=已购买
 */
@Getter
public enum UserTaSell
{
    NO_SELL(Integer.valueOf(1), "未购买"), SELL_OK(2, "已购买");

    private final Integer code;
    private final String info;

    UserTaSell(Integer code, String info)
    {
        this.code = code;
        this.info = info;
    }

}
