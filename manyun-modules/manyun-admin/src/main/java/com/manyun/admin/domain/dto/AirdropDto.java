package com.manyun.admin.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("空投请求参数")
public class AirdropDto {

    @ApiModelProperty("手机号")
    @NotBlank(message= "手机号不可以为空")
    private String phone;

    @ApiModelProperty("藏品id")
    @NotBlank(message = "藏品id不可以为空")
    private String collectionId;

}
