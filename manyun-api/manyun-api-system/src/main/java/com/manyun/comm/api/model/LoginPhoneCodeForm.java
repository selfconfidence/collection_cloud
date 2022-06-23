package com.manyun.comm.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@ApiModel("用户手机验证码提交表单")
public class LoginPhoneCodeForm implements Serializable {


    @ApiModelProperty("登录手机号")
    @NotBlank(message = "手机号不可为空")
    private String phone;

    @ApiModelProperty("手机号验证码")
    @NotBlank(message = "手机号验证码不可为空")
    private String phoneCode;
}
