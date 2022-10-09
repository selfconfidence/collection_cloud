package com.manyun.business.design.pay.bean.changePayPass;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "找回密码验证提交参数")
public class ChangePayPassVerifyForm {

    @ApiModelProperty("授权令牌，有效期为30分钟。")
    @NotBlank
    private String token;

    @ApiModelProperty("短信验证码")
    @NotBlank
    private String verify_code;

    @ApiModelProperty("密码随机因子key")
    @NotBlank
    private String random_key;

    @ApiModelProperty("新支付密码")
    @NotBlank
    private String password;

}
