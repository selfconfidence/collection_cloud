package com.manyun.admin.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("藏品字典")
public class CntCollectionDictVo {

    @ApiModelProperty("藏品id")
    private String id;

    @ApiModelProperty("藏品名称")
    private String collectionName;

}
