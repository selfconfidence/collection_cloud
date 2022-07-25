package com.manyun.comm.api.domain.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("实名认证提交表单")
@Data
public class UserRealMoneyForm {

    @ApiModelProperty("银行卡号")
    private String bankcard;
}
