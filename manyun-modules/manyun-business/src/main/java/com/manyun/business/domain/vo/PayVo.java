package com.manyun.business.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("支付返回视图")
public class PayVo implements Serializable {

    @ApiModelProperty("支付编号")
    private String outHost;

    @ApiModelProperty("支付信息，用来拉起支付")
    private String body;


    @ApiModelProperty(value = "0=余额支付，1=微信,2=支付宝,3=银联",notes = "" +
            "如果为 0 的话,代表是余额支付,对应的 body 不会有值!" +
            "其他支付类型的话  body 均会有值,用来拉起支付")
    private Integer payType;
}
