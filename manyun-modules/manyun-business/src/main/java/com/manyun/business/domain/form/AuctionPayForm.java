package com.manyun.business.domain.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@ApiModel("拍卖市场提交表单")
public class AuctionPayForm implements Serializable {

    @ApiModelProperty(value = "竞品id", required = true)
    @NotBlank(message = "竞品编号不可为空")
    private String auctionSendId;

    @ApiModelProperty(value = "支付类型,1=微信,2=支付宝,0=余额支付，3=银联, 4=杉德,5=连连",required = true)
    @Range(min = 0,max = 5,message = "支付类型错误")
    private Integer payType;

    @ApiModelProperty(value = "是否余额抵扣, true抵扣，false不抵扣", required = true)
    private boolean isDeduction;

    @ApiModelProperty("原生双端 h5所需,支付后返回app 的路径")
    @NotBlank(message = "跳转url不可为空")
    private String returnUrl;

    @ApiModelProperty("支付密码")
    private String payPass;
}
