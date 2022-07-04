package com.manyun.common.core.enums;

import lombok.Getter;

/**
 * 寄售平台打款状态
 *
 * @author yanwei
 *
 * 0待打款,1=已打款
 */
@Getter
public enum ConsignmentToPayStatus
{
    WAIT_TO_PAY(Integer.valueOf(0), "待打款"), OK_TO_PAY(1, "已打款");

    private final Integer code;
    private final String info;

    ConsignmentToPayStatus(Integer code, String info)
    {
        this.code = code;
        this.info = info;
    }

}
