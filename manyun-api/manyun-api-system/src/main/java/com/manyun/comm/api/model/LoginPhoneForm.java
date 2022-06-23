package com.manyun.comm.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@ApiModel("用户提交表单")
public class LoginPhoneForm  implements Serializable {


    @ApiModelProperty("登录手机号")
    @NotBlank(message = "手机号不可为空")
    private String phone;

    @ApiModelProperty("登录密码")
    @NotBlank(message = "登录密码不可为空")
    private String password;
}
