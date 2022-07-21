package com.manyun.admin.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("藏品分类返回视图")
public class CntCateVo {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("系列名称")
    private String cateName;

}
