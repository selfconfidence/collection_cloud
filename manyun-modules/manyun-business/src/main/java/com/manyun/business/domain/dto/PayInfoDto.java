package com.manyun.business.domain.dto;

import com.manyun.common.core.enums.*;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 支付内部参项
 */
@Data
@Builder
public class PayInfoDto implements Serializable {

    //商品名称
    private String goodsName;
    //订单号
    private String outHost;
    //金额
    private BigDecimal realPayMoney;
    //用户id
    private String userId;
    //ip
    private String ipaddr;
    //支付方式
    private Integer payType;
    private AliPayEnum aliPayEnum;
    private WxPayEnum wxPayEnum;
    private ShandePayEnum shandePayEnum;
    private LianLianPayEnum lianlianPayEnum;
    private SandAccountEnum sandAccountEnum;

    //收款方用户id
    private String receiveUserId;

    //是否可以用户之间交易
    private boolean canTrade;

    //服务费，连连支付需传
    private BigDecimal serviceCharge;

    //C2C(TRUE),C2B(FALESE)
    private boolean c2c;






}
