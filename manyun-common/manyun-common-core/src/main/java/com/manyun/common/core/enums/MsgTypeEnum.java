package com.manyun.common.core.enums;

import lombok.Getter;

/**
 * 消息类型
 * 
 * @author yanwei
 *
 *  1=可对外，2=不可对外
 */
@Getter
public enum MsgTypeEnum
{
    OK_OPEN_MSG(Integer.valueOf(1), "可对外"), NO_OPEN_MSG(2, "不可对外");

    private final Integer code;
    private final String info;

    MsgTypeEnum(Integer code, String info)
    {
        this.code = code;
        this.info = info;
    }

}
