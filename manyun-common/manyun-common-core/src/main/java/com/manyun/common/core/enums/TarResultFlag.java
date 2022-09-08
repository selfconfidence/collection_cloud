package com.manyun.common.core.enums;

import lombok.Getter;

/**
 * 1=正常,2=暂停(已经开奖)
 * 
 * @author yanwei
 *
 *1=正常,2=暂停(已经开奖)
 */
@Getter
public enum TarResultFlag
{
    FLAG_PROCESS(Integer.valueOf(1), "正常"), FLAG_STOP(2, "暂停(已经开奖)");

    private final Integer code;
    private final String info;

    TarResultFlag(Integer code, String info)
    {
        this.code = code;
        this.info = info;
    }

}
