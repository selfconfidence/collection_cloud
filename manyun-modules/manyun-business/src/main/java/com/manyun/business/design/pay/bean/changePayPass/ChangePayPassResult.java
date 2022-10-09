package com.manyun.business.design.pay.bean.changePayPass;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@ApiModel(value = "修改连连支付密码响应参数")
public class ChangePayPassResult {

    @ApiModelProperty("状态码")
    private String ret_code;

    @ApiModelProperty("请求结果描述")
    private String ret_msg;

    @ApiModelProperty("商户号")
    private String oid_partner;

    @ApiModelProperty("用户id")
    private String user_id;

    @ApiModelProperty("授权令牌。有效期30分钟")
    private String token;

    @ApiModelProperty("手机号")
    private String reg_phone;


}
