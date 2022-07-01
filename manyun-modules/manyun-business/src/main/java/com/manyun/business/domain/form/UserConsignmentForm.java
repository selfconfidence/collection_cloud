package com.manyun.business.domain.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("寄售提交表单")
public class UserConsignmentForm implements Serializable {


    @ApiModelProperty("转赠类型 0藏品，1盲盒")
    @Range(min = 0L,max = 1L,message = "转赠类型有误")
    private Integer type;

    @ApiModelProperty("(用户拥有藏品返回视图|我的盲盒基本信息) 两个视图中的 id")
    @NotBlank(message = "基础信息不可为空")
    private String buiId;

    @ApiModelProperty("寄售价格")
    @NotNull(message = "寄售价格不可为空")
    private BigDecimal consignmentMoney;


}
