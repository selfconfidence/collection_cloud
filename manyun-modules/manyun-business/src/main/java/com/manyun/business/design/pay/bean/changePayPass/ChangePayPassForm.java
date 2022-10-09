package com.manyun.business.design.pay.bean.changePayPass;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
@ApiModel(value = "找回密码提交参数")
public class ChangePayPassForm {

    /*@ApiModelProperty(value = "user_id，用户在商户系统中的唯一编号",required = true)
    @NotBlank(message = "用户id不能为空!")
    private String user_id;*/

    @ApiModelProperty(value = "绑定银行卡号")
    @NotBlank
    private String linked_acctno;

}
