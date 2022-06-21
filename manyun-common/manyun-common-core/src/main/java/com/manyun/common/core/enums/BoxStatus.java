package com.manyun.common.core.enums;

import lombok.Getter;

/**
 * 盲盒状态
 * 
 * @author yanwei
 *
 * 0=下架,1=正常,2=售罄
 */
@Getter
public enum BoxStatus
{
    DOWN_ACTION(Integer.valueOf(0), "下架"), UP_ACTION(1, "正常"), NULL_ACTION(2, "售罄");

    private final Integer code;
    private final String info;

    BoxStatus(Integer code, String info)
    {
        this.code = code;
        this.info = info;
    }

}
