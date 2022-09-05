package com.manyun.admin.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("订单管理请求参数")
public class OrderInfoDto {

    @ApiModelProperty(value = "id",required = true)
    @NotBlank(message= "缺失必要参数!")
    private String id;

}
