package com.manyun.common.core.enums;

/**
 * 邀请盲盒 用户转换套
 *1=待领取,2=已领取，3未满足条件
 * @author yanwei
 */
public enum PleaseBoxProcessStatus
{
    WAIT_PRO(Integer.valueOf(1), "待领取"), OK_PRO(Integer.valueOf(2), "已经领取"), NO_PRO(Integer.valueOf(3), "未满足条件");

    private final Integer code;
    private final String info;

    PleaseBoxProcessStatus(Integer code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public Integer getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }
}
