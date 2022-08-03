package com.manyun.admin.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("打款审核请求参数")
public class PaymentReviewDto {

    @ApiModelProperty("寄售单号")
    @NotBlank(message= "寄售单号不能为空")
    private String id;

}
