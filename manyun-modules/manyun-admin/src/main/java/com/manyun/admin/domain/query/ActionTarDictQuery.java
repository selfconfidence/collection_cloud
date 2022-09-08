package com.manyun.admin.domain.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@ApiModel("活动材料字典请求参数")
@Data
public class ActionTarDictQuery {

    @NotBlank(message = "活动id不能为空")
    @ApiModelProperty(value = "活动id",required = true)
    private String actionId;

}
