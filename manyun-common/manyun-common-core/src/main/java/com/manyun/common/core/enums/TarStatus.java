package com.manyun.common.core.enums;

import lombok.Getter;

/**
 * 抽签状态
 * 
 * @author yanwei
 *
 *0不需要抽签,1=已抽中,2=未抽中,3=未抽签
 */
@Getter
public enum TarStatus
{
    NO_TAR(Integer.valueOf(0), "无需抽签"), CEN_YES_TAR(1, "已抽中"), CEN_NO_TAR(2, "未抽中"), CEN_NOT_TAR(3, "未抽签"), CEN_WAIT_TAR(4, "等待开奖");

    private final Integer code;
    private final String info;

    TarStatus(Integer code, String info)
    {
        this.code = code;
        this.info = info;
    }

}
