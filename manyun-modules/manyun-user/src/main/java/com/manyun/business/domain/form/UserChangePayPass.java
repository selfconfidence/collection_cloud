package com.manyun.business.domain.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@ApiModel("修改支付密码")
public class UserChangePayPass  implements Serializable {


    @ApiModelProperty("手机验证码")
    @NotBlank(message = "手机验证码不可为空")
    private String phoneCode;

    @ApiModelProperty("支付密码")
    @NotBlank(message = "支付密码不可为空")
    private String newPayPass;




}
