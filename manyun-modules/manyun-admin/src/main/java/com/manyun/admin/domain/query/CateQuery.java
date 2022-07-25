package com.manyun.admin.domain.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("系列分类条件查询对象")
@Data
public class CateQuery {

    @ApiModelProperty("类目名称")
    private String cateName;

}
