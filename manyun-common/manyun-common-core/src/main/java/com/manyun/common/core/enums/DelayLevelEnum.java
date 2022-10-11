package com.manyun.common.core.enums;

import lombok.Getter;

/**
 * 消息延时等级
 *
 * @author yanwei
 */
@Getter
public enum DelayLevelEnum
{//1s 1m 5m 10m 20m 30m 1h 2h 3h 5h 8h 10h 1d 2d 3d 5d 7d 8d
    LEVEL_1(1, "1s"),
    LEVEL_2(2, "1m"),
    LEVEL_3(3, "5m"),
    LEVEL_4(4, "10m"),
    LEVEL_5(5, "20m"),
    LEVEL_6(6, "30m"),
    LEVEL_7(7, "1h"),
    LEVEL_8(8, "2h"),
    LEVEL_9(9, "3h"),
    LEVEL_10(10, "5h"),
    LEVEL_11(11, "8h"),
    LEVEL_12(12, "10h"),
    LEVEL_13(13, "1d"),
    LEVEL_14(14, "2d"),
    LEVEL_15(15, "3d"),
    LEVEL_16(16, "5d"),
    LEVEL_17(17, "7d"),
    LEVEL_18(18, "8d");

    private final Integer delay;
    private final String info;

    DelayLevelEnum(Integer delay, String info)
    {
        this.delay = delay;
        this.info = info;
    }


}
