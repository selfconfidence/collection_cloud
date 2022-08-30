package com.manyun.admin.domain.query;

import com.manyun.common.core.web.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("轮播跳转链接条件查询")
@Data
public class BannerJumpLinkQuery extends PageQuery
{

    @ApiModelProperty("链接名称")
    private String linkName;

}
