package com.manyun.common.core.enums;

import lombok.Getter;

/**
 * 公告状态
 * 
 * @author yanwei
 *
 *（0正常 1关闭）
 */
@Getter
public enum NoticeStatus
{
    NOTICE_STATUS_OK(Integer.valueOf(0), "正常"), NOTICE_STATUS_DISABLE(1, "关闭");

    private final Integer code;
    private final String info;

    NoticeStatus(Integer code, String info)
    {
        this.code = code;
        this.info = info;
    }

}
