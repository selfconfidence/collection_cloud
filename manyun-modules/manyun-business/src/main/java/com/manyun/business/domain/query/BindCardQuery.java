package com.manyun.business.domain.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("新增绑卡请求参数")
public class BindCardQuery {

    @ApiModelProperty(value = "预留手机号",required = true)
    @NotBlank(message = "预留手机号不能为空!")
    private String linkedPhone;

    @ApiModelProperty(value = "银行卡号",required = true)
    @NotBlank(message = "银行卡号不能为空!")
    private String linkedAcctno;

    @ApiModelProperty(value = "支付密码",required = true)
    @NotBlank(message = "支付密码不能为空!")
    private String password;

    @ApiModelProperty(value = "密码随机因子key",required = true)
    @NotBlank(message = "密码随机因子key不能为空!")
    private String randomKey;

}
