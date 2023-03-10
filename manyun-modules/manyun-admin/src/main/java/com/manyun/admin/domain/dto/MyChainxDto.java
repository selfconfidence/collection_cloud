package com.manyun.admin.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@ApiModel("重新上链请求参数")
@Data
public class MyChainxDto {

    @NotBlank(message = "参数主键id不能为空")
    @ApiModelProperty(value = "用户和藏品表主键",required = true)
    private String id;

    @NotBlank(message = "参数用户id不能为空")
    @ApiModelProperty(value = "用户id",required = true)
    private String userId;

}
