package com.manyun.admin.domain.query;

import com.manyun.common.core.web.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("合成目标藏品条件查询")
@Data
public class ActionCollectionQuery extends PageQuery {

    @ApiModelProperty("活动编号")
    private String actionId;

}
