package com.manyun.admin.domain.query;

import com.manyun.common.core.web.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("创作者条件查询对象")
@Data
public class CreationdQuery extends PageQuery {

    @ApiModelProperty("创作者名称")
    private String creationName;

}
