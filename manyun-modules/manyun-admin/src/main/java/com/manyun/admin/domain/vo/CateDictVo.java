package com.manyun.admin.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("藏品分类字典")
public class CateDictVo {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("系列名称")
    private String cateName;

}
