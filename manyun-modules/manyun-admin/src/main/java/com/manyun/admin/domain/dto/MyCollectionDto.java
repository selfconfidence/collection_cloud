package com.manyun.admin.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@ApiModel("我的藏品")
@Data
public class MyCollectionDto {

    @ApiModelProperty(value = "用户id",required = true)
    @NotBlank(message= "用户id不能为空!")
    private String userId;

}
