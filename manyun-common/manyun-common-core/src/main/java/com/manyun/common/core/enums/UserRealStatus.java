package com.manyun.common.core.enums;

/**
 * 用户实名状态
 * 是否实名;1=未实名,2=实名
 * @author yanwei
 */
public enum UserRealStatus
{
    NO_REAL(Integer.valueOf(1), "未实名"), OK_REAL(Integer.valueOf(2), "实名");

    private final Integer code;
    private final String info;

    UserRealStatus(Integer code, String info)
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
