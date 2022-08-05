package com.manyun.business.domain.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@ApiModel("用户根据验证码修改密码表单")
public class UserChangeCodeLoginForm implements Serializable {


    @ApiModelProperty(required = true,value = "密码")
    @NotBlank(message = "密码不可为空")
    private String passWord;


    @ApiModelProperty("手机验证码")
    @NotBlank(message = "手机验证码不可为空")
    private String phoneCode;


}
