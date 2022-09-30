package com.manyun.business.design.pay.bean.queryPayment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@ApiModel(value = "交易查询结果付款方信息响应参数")
public class PayerInfo {

    @ApiModelProperty("付款方类型 用户：USER  平台商户：MERCHANT")
    private String payer_type;

    @ApiModelProperty("付款方标识 付款方为用户时设置user_id 付款方为商户时设置平台商户号。")
    private String payer_id;

    @ApiModelProperty("付款方式")
    private String method;

    @ApiModelProperty("付款金额")
    private String amount;

}
