package com.manyun.common.core.enums;

import lombok.Getter;

/**
 * 订单状态
 * 
 * @author yanwei
 *
 *  1=藏品系列，2=盲盒分类
 */
@Getter
public enum CateType
{
   COLLECTION_CATE(1, "藏品系列"), BOX_CATE(2, "已取消");

    private final Integer code;
    private final String info;

    CateType(Integer code, String info)
    {
        this.code = code;
        this.info = info;
    }

}
