package com.manyun.admin.domain.query;

import com.manyun.common.core.web.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@ApiModel("提前购配置已经拥有条件查询")
@Data
public class PostExistQuery extends PageQuery
{

    @ApiModelProperty("业务名称")
    private String buiName;

    @ApiModelProperty("藏品名称")
    private String collectionId;

    @ApiModelProperty("配置名称")
    private String configId;

}
