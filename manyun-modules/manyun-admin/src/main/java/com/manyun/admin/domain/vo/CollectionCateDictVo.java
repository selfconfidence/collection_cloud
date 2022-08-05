package com.manyun.admin.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("藏品分类字典")
public class CollectionCateDictVo {

    @ApiModelProperty("分类id")
    private String id;

    @ApiModelProperty("系列名称")
    private String cateName;

    @ApiModelProperty("创作者编号")
    private String bindCreation;

}
