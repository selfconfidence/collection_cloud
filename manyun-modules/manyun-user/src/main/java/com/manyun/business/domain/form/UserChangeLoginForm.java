package com.manyun.business.domain.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@ApiModel("用户修改密码表单")
public class UserChangeLoginForm implements Serializable {

    @ApiModelProperty(required = true,value = "旧密码")
    @NotBlank(message = "旧密码不可为空")
    private String oldPass;

    @ApiModelProperty(required = true,value = "新密码")
    @NotBlank(message = "新密码不可为空")
    private String newPass;


}
