package com.manyun.admin.domain.query;

import com.manyun.common.core.web.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("盲盒评分条件查询对象")
@Data
public class BoxScoreQuery extends PageQuery {

    @ApiModelProperty("评分名称")
    private String scoreName;

}
