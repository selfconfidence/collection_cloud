package com.manyun.admin.domain.query;

import com.manyun.common.core.web.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@ApiModel("发行放条件查询")
@Data
public class IssuanceQuery extends PageQuery
{

    @ApiModelProperty("发行方名称")
    private String publishOther;

}
