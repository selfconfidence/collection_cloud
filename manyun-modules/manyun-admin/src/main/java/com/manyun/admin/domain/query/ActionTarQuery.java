package com.manyun.admin.domain.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("活动材料条件查询对象")
@Data
public class ActionTarQuery {

    @ApiModelProperty("活动编号")
    private String actionId;

}
