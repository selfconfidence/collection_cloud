package com.manyun.admin.domain.query;

import com.manyun.common.core.web.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("盲盒与藏品中间条件查询对象")
@Data
public class BoxCollectionQuery extends PageQuery {

    @ApiModelProperty("盲盒编号")
    private String boxId;

}
