package com.manyun.common.core.enums;

import lombok.Getter;

/**
 * 藏品
 * 
 * @author yanwei
 *
 *;1=未上链,2=已上链
 */
@Getter
public enum CollectionLink
{
    NOT_LINK(Integer.valueOf(1), "未上链"), OK_LINK(2, "已上链");

    private final Integer code;
    private final String info;

    CollectionLink(Integer code, String info)
    {
        this.code = code;
        this.info = info;
    }

}
