package com.manyun.common.core.enums;

import lombok.Getter;

/**
 * 盲盒状态
 * 
 * @author yanwei
 *
 *抽签类型;(1=盲盒,2=藏品)
 */
@Getter
public enum TarType
{
    BOX_TAR(Integer.valueOf(1), "盲盒"), COLLECTION_TAR(2, "藏品");

    private final Integer code;
    private final String info;

    TarType(Integer code, String info)
    {
        this.code = code;
        this.info = info;
    }

}
