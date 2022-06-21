package com.manyun.business.domain.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel("购买盲盒提交表单")
public class BoxSellForm implements Serializable {

    @ApiModelProperty(value = "盲盒编号",required = true)
    @NotBlank(message = "盲盒编号不可为空")
    private String boxId;



    @ApiModelProperty(value = "支付类型,1=微信,2=支付宝,0=余额支付，3=银联",required = true)
    @Range(min = 0,max = 3,message = "支付类型错误")
    private Integer payType;

    @ApiModelProperty(value = "购买数量",required = true)
    @NotNull(message = "购买数量不可为空")
    @Range(min = 1,message = "最少1件起售")
    private Integer sellNum;

}
