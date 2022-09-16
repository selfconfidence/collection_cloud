package com.manyun.business.domain.dto;

import com.manyun.common.core.enums.AliPayEnum;
import com.manyun.common.core.enums.LianLianPayEnum;
import com.manyun.common.core.enums.ShandePayEnum;
import com.manyun.common.core.enums.WxPayEnum;
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

    //收款方用户id
    private String receiveUserId;

    //是否可以用户之间交易
    private boolean canTrade;

    //服务费，连连支付需传
    private BigDecimal serviceCharge;






}
