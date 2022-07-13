package com.manyun.business.domain.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("一口价提交表单")
public class AuctionPayMarginForm {

    @ApiModelProperty(value = "竞品id", required = true)
    @NotBlank(message = "竞品编号不可为空")
    private String auctionSendId;

    @ApiModelProperty(value = "支付类型,1=微信,2=支付宝,0=余额支付，3=银联",required = true)
    @Range(min = 0,max = 3,message = "支付类型错误")
    private Integer payType;

    @ApiModelProperty(value = "是否余额抵扣, true抵扣，false不抵扣", required = true)
    private boolean isDeduction;
}
