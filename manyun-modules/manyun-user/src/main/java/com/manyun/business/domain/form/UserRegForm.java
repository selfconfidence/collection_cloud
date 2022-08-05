package com.manyun.business.domain.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@ApiModel("用户注册表单")
public class UserRegForm implements Serializable {

    public UserRegForm(String phone) {
        this.phone = phone;
    }

    public UserRegForm() {
    }

    @ApiModelProperty("手机号")
    private String phone;


    @ApiModelProperty("手机号验证码")
    private String phoneCode;

    @ApiModelProperty("邀请码 - 没有则不填")
    private String pleaseCode;

    @ApiModelProperty("登录密码")
    @NotBlank(message = "登录密码不可为空")
    private String loginPassWord;


}
