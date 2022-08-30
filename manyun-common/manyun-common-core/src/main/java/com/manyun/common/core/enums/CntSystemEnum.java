package com.manyun.common.core.enums;

/**
 * 平台规则
 */
public enum CntSystemEnum
{

    INVITE_URL("invite_url"),
    USER_DEFAULT_LINK("USER_DEFAULT_LINK");

    private final String type;

    CntSystemEnum(String type) {
        this.type = type;
    }

}
