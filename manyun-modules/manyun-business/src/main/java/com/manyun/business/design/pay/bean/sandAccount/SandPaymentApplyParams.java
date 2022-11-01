package com.manyun.business.design.pay.bean.sandAccount;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("杉德付款申请参数")
public class SandPaymentApplyParams {

    @ApiModelProperty("订单金额")
    private BigDecimal orderAmt;

    @ApiModelProperty("付款方会员编号")
    private String bizUserNo;

    @ApiModelProperty("收款方会员编号")
    private String payeeUserNo;

    @ApiModelProperty("收款订单金额")
    private BigDecimal payeeAmt;

    @ApiModelProperty("主收款人编号")
    private String payeeBizUserNo;

    @ApiModelProperty("订单标题")
    private String orderSubject;

    @ApiModelProperty("订单描述")
    private String orderDesc;

    @ApiModelProperty("前台跳转地址")
    private String frontUrl;

    @ApiModelProperty("异步通知地址")
    private String notifyUrl;


}
