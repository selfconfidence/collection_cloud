package com.manyun.admin.domain.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("发行者条件查询对象")
@Data
public class CollectionInfoQuery {

    @ApiModelProperty("发行方")
    private String publishOther;

}
