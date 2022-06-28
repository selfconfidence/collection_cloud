package com.manyun.common.core.enums;

import lombok.Getter;

@Getter
public enum AuctionSendStatus {
    WAIT_START(Integer.valueOf(0),"待开始"), BID_BIDING(1, "竞拍中"), WAIT_PAY(2, "待支付"),
    BID_SUCCESS(3, "已支付"), BID_BREAK(4, "已违约"), BID_PASS(5, "已流拍");

    private final Integer code;
    private final String info;

    AuctionSendStatus(Integer code, String info) {
        this.code = code;
        this.info = info;
    }
}
