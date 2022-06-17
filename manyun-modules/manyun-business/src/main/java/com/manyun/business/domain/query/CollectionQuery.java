package com.manyun.business.domain.query;

import com.manyun.common.core.web.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("藏品查询相关条件")
public class CollectionQuery extends PageQuery {

    @ApiModelProperty("藏品名称_模糊查询")
    private String collectionName;

    @ApiModelProperty("创作者编号")
    private String bindCreationId;

    @ApiModelProperty("系列编号")
    private String cateId;




}
