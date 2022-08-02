package com.manyun.admin.domain.query;

import com.manyun.common.core.web.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("活动记录条件查询对象")
@Data
public class ActionRecordQuery extends PageQuery {

    @ApiModelProperty("活动标题")
    private String actionTitle;

}
