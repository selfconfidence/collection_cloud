package com.manyun.common.core.enums;

import lombok.Getter;

@Getter
public enum AuctionSendStatus {
    WAIT_START(Integer.valueOf(1),"未开拍"), BID_BIDING(2, "竞拍中"), WAIT_PAY(3, "待支付"),
    BID_SUCCESS(4, "已完成"), BID_BREAK(5, "已违约"), BID_PASS(6, "已流拍");

    private final Integer code;
    private final String info;

    AuctionSendStatus(Integer code, String info) {
        this.code = code;
        this.info = info;
    }
}
