package com.manyun.admin.domain.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("藏品条件查询对象")
@Data
public class CollectionQuery {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("藏品名称")
    private String collectionName;

    @ApiModelProperty("藏品状态;0=下架,1=正常,2=售罄")
    private Integer statusBy;

}
