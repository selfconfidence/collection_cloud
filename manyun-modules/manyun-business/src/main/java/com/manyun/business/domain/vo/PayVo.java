package com.manyun.business.domain.vo;

import cn.hutool.core.util.NumberUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("支付返回视图")
public class PayVo implements Serializable {

    @ApiModelProperty("支付编号")
    private String outHost;

    @ApiModelProperty("支付信息，用来拉起支付 - 如果为null 代表不用拉起支付,反之需要拉起支付")
    private String body;

    private transient BigDecimal moneyBln = NumberUtil.add(0D);

    @ApiModelProperty(value = "0=余额支付，1=微信,2=支付宝,3=银联")
    private Integer payType;

    private transient String txnSeqno;
}
