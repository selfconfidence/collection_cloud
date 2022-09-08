package com.manyun.admin.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("合成活动状态检查")
public class ActionCollectionDto {

    @ApiModelProperty("活动Id")
    private String actionId;

    @ApiModelProperty("当前活动目标藏品总库存")
    private Integer totalQuantity;

}
