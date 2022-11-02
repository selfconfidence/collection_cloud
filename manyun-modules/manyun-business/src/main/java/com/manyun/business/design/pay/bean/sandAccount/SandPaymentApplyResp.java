package com.manyun.business.design.pay.bean.sandAccount;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("杉德支付申请响应参数")
public class SandPaymentApplyResp {

    @ApiModelProperty("鉴权方式:01：发送短信（有效时间5分钟）\n" +
            "02：支付密码\n" +
            "03：免密免短信（无需确认支付）\n" +
            "04：支付密码+短信")
    private String authWay;

    @ApiModelProperty("手续费")
    private String feeAmt;

    @ApiModelProperty("密码页面链接")
    private String passwordURL;
}
