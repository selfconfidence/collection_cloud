package com.manyun.common.core.enums;

import lombok.Getter;

/**
 * 寄售状态
 *
 * @author yanwei
 *
 * 1=已寄售,2=已锁单(有买方,未支付而已)  3=已售出
 */
@Getter
public enum ConsignmentStatus
{
    PUSH_CONSIGN(Integer.valueOf(1), "已寄售"), LOCK_CONSIGN(2, "已锁单"), OVER_CONSIGN(3, "已售出");

    private final Integer code;
    private final String info;

    ConsignmentStatus(Integer code, String info)
    {
        this.code = code;
        this.info = info;
    }

}
