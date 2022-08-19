package com.manyun.admin.domain.query;

import com.manyun.common.core.web.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("系列分类条件查询对象")
@Data
public class CateQuery extends PageQuery {

    @ApiModelProperty("类目名称")
    private String cateName;

    @ApiModelProperty("父菜单名称")
    private String parentId;

    @ApiModelProperty("1=藏品系列，2=盲盒分类")
    private Integer cateType;

}
