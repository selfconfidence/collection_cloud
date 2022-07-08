package com.manyun.common.core.enums;

import lombok.Getter;

/**
 * 竞拍状态
 * 1竞拍中，2未拍中，3待支付，4已支付，5已违约，6已违约，7已流拍
 */
@Getter
public enum AuctionStatus {
    WAIT_START(Integer.valueOf(1),"待开始"), BID_BIDING(2, "竞拍中"), BID_MISSED(3, "未拍中"), WAIT_PAY(4, "待支付"),
    PAY_SUCCESS(5, "已支付"), BID_BREAK(6, "已违约"), BID_PASS(7, "已流拍");

    private final Integer code;
    private final String info;

    AuctionStatus(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

}
