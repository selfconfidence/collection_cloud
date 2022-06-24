package com.manyun.common.core.enums;

/**
 * 邀请盲盒奖励 使用状态
 *是否使用;1=使用，2=未使用
 * @author yanwei
 */
public enum PleaseBoxStatus
{
    OK_USE(Integer.valueOf(1), "使用"), NO_USE(Integer.valueOf(2), "未使用");

    private final Integer code;
    private final String info;

    PleaseBoxStatus(Integer code, String info)
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
