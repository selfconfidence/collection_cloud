package com.manyun.business.domain.query;

import com.manyun.common.core.web.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("盲盒查询相关条件")
public class BoxQuery extends PageQuery {

    @ApiModelProperty("盲盒名称_模糊查询")
    private String boxName;


    @ApiModelProperty("分类编号")
    private String cateId;


}
