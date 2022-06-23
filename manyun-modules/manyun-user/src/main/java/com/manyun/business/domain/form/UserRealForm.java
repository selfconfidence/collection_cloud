package com.manyun.business.domain.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@ApiModel("用户实名认证信息表单")
public class UserRealForm implements Serializable {

    @ApiModelProperty(required = true,value = "真实姓名")
    @NotBlank(message = "真实姓名不可为空")
    private String realName;

    @ApiModelProperty(required = true,value = "手机号")
    @NotBlank(message = "手机号不可为空")
    private String phone;

    @ApiModelProperty(required = true,value = "身份证号")
    @NotBlank(message = "身份证号不可为空")
    private String cartNo;

}
