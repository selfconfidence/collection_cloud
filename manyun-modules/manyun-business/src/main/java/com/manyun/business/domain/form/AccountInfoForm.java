package com.manyun.business.domain.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@ApiModel("收款信提交表单")
public class AccountInfoForm implements Serializable {

    @ApiModelProperty("真实姓名")
    @NotBlank(message = "真实姓名不可为空")
    private String realName;

    @ApiModelProperty("真实手机号")
    @NotBlank(message = "真实手机号不可为空")
    private String realPhone;


    @ApiModelProperty("身份证正面_图片链接")
    @NotBlank(message = "身份证正面不可为空")
    private String cartJust;

    @ApiModelProperty("身份证反面_图片链接")
    @NotBlank(message = "身份证反面不可为空")
    private String cartBack;


}
