package com.manyun.business.design.pay.bean.sandAccount;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("杉德开户提交参数")
public class SandOpenAccountParams {

    @ApiModelProperty("会员编号")
    private String bizUserNo;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("证件号码")
    private String idNo;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("短信验证码")
    private String smsCode;

    @ApiModelProperty("短信流水号")
    private String smsSerialNo;

    @ApiModelProperty("卡号")
    private String cardNo;
    @ApiModelProperty("银行预留手机号")
    private String bankMobile;


}
