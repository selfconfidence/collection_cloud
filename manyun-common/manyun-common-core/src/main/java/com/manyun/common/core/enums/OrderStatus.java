package com.manyun.common.core.enums;

import lombok.Getter;

/**
 * 订单状态
 * 
 * @author yanwei
 *
 * 0待付款，1已完成，2已取消，-1支付未回调，3=进行中(这个比较特殊 属于寄售的时候用的)
 */
@Getter
public enum OrderStatus
{
    WAIT_ORDER(Integer.valueOf(0), "待付款"), OVER_ORDER(1, "已完成"), CANCEL_ORDER(2, "已取消"), NO_NOD_ORDER(-1, "支付未回调"), PROCESS_ORDER(3, "进行中(寄售专区特用)");

    private final Integer code;
    private final String info;

    OrderStatus(Integer code, String info)
    {
        this.code = code;
        this.info = info;
    }

}
