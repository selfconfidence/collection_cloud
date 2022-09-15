package com.manyun.business.domain.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "交易二次短信验证请求参数")
public class LLValidationSmsQuery {

    @ApiModelProperty("流水号")
    @NotBlank(message = "流水号不能为空!")
    private String txnSeqno;

    @ApiModelProperty("金额")
    @NotBlank(message = "金额不能为空!")
    private String amount;

    @ApiModelProperty("授权令牌")
    @NotBlank(message = "授权令牌不能为空!")
    private String token;

    @ApiModelProperty("短信验证码")
    @NotBlank(message = "短信验证码不能为空!")
    private String verifyCode;

}
