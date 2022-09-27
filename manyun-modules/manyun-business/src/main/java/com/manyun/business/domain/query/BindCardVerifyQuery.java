package com.manyun.business.domain.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("新增绑卡验证请求参数")
public class BindCardVerifyQuery {

    @ApiModelProperty(value = "流水号",required = true)
    @NotBlank(message = "流水号不能为空!")
    private String txnSeqno;

    @ApiModelProperty(value = "授权令牌",required = true)
    @NotBlank(message = "授权令牌不能为空!")
    private String token;

    @ApiModelProperty(value = "短信验证码",required = true)
    @NotBlank(message = "短信验证码不能为空!")
    private String verifyCode;

}
