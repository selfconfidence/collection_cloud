package com.manyun.business.domain.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("用户注册表单")
public class UserRegForm implements Serializable {


    @ApiModelProperty("手机号")
    private String phone;


    @ApiModelProperty("手机号验证码")
    private String phoneCode;

    @ApiModelProperty("邀请码 - 没有则不填")
    private String pleaseCode;


}
