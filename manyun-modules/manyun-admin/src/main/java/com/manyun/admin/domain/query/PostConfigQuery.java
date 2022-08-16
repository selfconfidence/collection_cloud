package com.manyun.admin.domain.query;

import com.manyun.common.core.web.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("提前购配置条件查询")
@Data
public class PostConfigQuery  extends  PageQuery {

    @ApiModelProperty("配置名称")
    private String configName;

}
