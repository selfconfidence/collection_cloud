package com.manyun.admin.domain.query;

import com.manyun.common.core.web.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("提前购可购买商品条件查询")
@Data
public class PostSellQuery extends PageQuery {

    @ApiModelProperty("配置编号")
    private String configId;

}
