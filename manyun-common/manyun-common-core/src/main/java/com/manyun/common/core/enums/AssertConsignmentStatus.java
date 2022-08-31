package com.manyun.common.core.enums;

import lombok.Getter;

/**
 * 是否推送寄售市场
 *
 * @author yanwei
 *
 * (0=可以，1=不可以)
 */
@Getter
public enum AssertConsignmentStatus
{
    OK_PUSH_CONSIGNMENT(Integer.valueOf(0), "可以"), FAIL_PUSH_CONSIGNMENT(1, "不可以");

    private final Integer code;
    private final String info;

    AssertConsignmentStatus(Integer code, String info)
    {
        this.code = code;
        this.info = info;
    }

}
