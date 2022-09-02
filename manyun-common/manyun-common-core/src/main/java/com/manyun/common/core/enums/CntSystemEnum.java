package com.manyun.common.core.enums;

/**
 * 平台规则
 */
public enum CntSystemEnum
{

    INVITE_URL("invite_url"),
    USER_DEFAULT_LINK("USER_DEFAULT_LINK"),
    SYNTHESIS_GIF("synthesis_gif"),
    OPEN_BOX_GIF("open_box_gif");

    private final String type;

    CntSystemEnum(String type) {
        this.type = type;
    }

}
