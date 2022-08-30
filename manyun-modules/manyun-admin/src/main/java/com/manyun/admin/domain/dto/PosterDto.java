package com.manyun.admin.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@ApiModel("平台规则")
@Data
public class PosterDto {

    @NotBlank(message = "参数不能为空!")
    @ApiModelProperty(value = "规则值",required = true)
    private String systemVal;

}
