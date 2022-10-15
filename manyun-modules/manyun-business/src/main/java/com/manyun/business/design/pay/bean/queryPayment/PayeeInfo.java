package com.manyun.business.design.pay.bean.queryPayment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@ApiModel(value = "交易查询结果收款方信息响应参数")
public class PayeeInfo {

    @ApiModelProperty("收款方类型 用户：USER 平台商户：MERCHANT")
    private String payee_type;

    @ApiModelProperty("收款方标识")
    private String payee_id;

    @ApiModelProperty("付款金额")
    private String amount;

}
