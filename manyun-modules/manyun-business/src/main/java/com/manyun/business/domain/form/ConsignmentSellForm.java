package com.manyun.business.domain.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel("寄售市场购买提交表单")
public class ConsignmentSellForm implements Serializable {

    @ApiModelProperty(value = "寄售编号",required = true)
    @NotBlank(message = "寄售编号不可为空")
    private String buiId;

    @ApiModelProperty(value = "支付类型,1=微信,2=支付宝,0=余额支付，3=银联",required = true)
    @Range(min = 0,max = 4,message = "支付类型错误")
    private Integer payType;



}
