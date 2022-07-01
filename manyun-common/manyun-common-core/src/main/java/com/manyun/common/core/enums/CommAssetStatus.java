package com.manyun.common.core.enums;

import lombok.Getter;

/**
 * 资产状态
 *
 * @author yanwei
 *
 *1=存在,2=不存在
 */
@Getter
public enum CommAssetStatus
{
    USE_EXIST(Integer.valueOf(1), "存在"), NOT_EXIST(2, "不存在");

    private final Integer code;
    private final String info;

    CommAssetStatus(Integer code, String info)
    {
        this.code = code;
        this.info = info;
    }

}
