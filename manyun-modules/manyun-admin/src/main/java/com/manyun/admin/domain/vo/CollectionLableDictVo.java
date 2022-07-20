package com.manyun.admin.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("藏品标签下拉框")
public class CollectionLableDictVo {

    @ApiModelProperty("藏品标签id")
    private String id;

    @ApiModelProperty("藏品标签名称")
    private String lableName;

}
