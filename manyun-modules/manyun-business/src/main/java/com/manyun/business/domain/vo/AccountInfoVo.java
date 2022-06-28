package com.manyun.business.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("收款信息返回视图")
public class AccountInfoVo  implements Serializable {

    @ApiModelProperty("真实姓名")
    private String realName;

    @ApiModelProperty("真实手机号")
    private String realPhone;


    @ApiModelProperty("身份证正面_图片链接")
    private String cartJust;

    @ApiModelProperty("身份证反面_图片链接")
    private String cartBack;

    @ApiModelProperty("是否绑定银行卡  1=绑定,2=未绑定")
    private Integer isBindCart;

}
