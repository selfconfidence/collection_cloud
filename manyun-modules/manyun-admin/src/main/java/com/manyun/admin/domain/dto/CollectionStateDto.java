package com.manyun.admin.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("更改藏品状态请求参数")
public class CollectionStateDto {

    @NotBlank(message = "藏品编号不可为空")
    @ApiModelProperty("主键")
    private String id;

    @NotNull(message = "藏品状态不可为空")
    @ApiModelProperty("藏品状态;0=下架,1=正常,2=售罄")
    private Integer statusBy;

}
