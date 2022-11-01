package com.manyun.business.design.pay.bean.sandAccount;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("杉德开户提交参数")
public class OpenAccountForm {

    @ApiModelProperty("跳转地址链接")
    private String returnUrl;
}
