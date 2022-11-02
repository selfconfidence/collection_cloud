package com.manyun.business.design.pay.bean.sandAccount;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("杉德验证码接口")
public class SandSmsSendParam {

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("手机号")
    private String phone;
}
