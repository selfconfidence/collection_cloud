package com.manyun.common.core.enums;

import lombok.Getter;

/**
 * 开启盲盒状态
 * 
 * @author yanwei
 *
 * 1未开启,2已开启
 */
@Getter
public enum BoxOpenType
{
   NO_OPEN(1, "未开启"), OK_OPEN(2, "已开启");

    private final Integer code;
    private final String info;

    BoxOpenType(Integer code, String info)
    {
        this.code = code;
        this.info = info;
    }

}
