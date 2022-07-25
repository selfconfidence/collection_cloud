package com.manyun.admin.domain.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("创作者条件查询对象")
@Data
public class CreationdQuery {

    @ApiModelProperty("创作者名称")
    private String creationName;

}
