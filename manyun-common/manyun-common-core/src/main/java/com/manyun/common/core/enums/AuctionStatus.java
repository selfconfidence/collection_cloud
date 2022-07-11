package com.manyun.common.core.enums;

import lombok.Getter;

/**
 * 竞拍状态
 * 1竞拍中，2未拍中，3待支付，4已支付，5已违约
 */
@Getter
public enum AuctionStatus {
    BID_BIDING(1, "竞拍中"), BID_MISSED(2, "未拍中"), WAIT_PAY(3, "待支付"),
    PAY_SUCCESS(4, "已支付"), BID_BREAK(5, "已违约");

    private final Integer code;
    private final String info;

    AuctionStatus(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

}
