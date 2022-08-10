package com.manyun.business.domain.query;

import com.manyun.common.core.web.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("我的藏品|盲盒列表查询条件")
public class UseAssertQuery extends PageQuery {

    @ApiModelProperty("搜索标题")
    private String commName;


}
