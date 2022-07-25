package com.manyun.admin.domain.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("活动条件查询对象")
@Data
public class ActionQuery {

    @ApiModelProperty("活动标题")
    private String actionTitle;

    @ApiModelProperty("活动状态;1=待开始,2=进行中,3=结束")
    private Long actionStatus;

}
