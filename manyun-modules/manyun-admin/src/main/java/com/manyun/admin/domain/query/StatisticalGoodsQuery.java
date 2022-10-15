package com.manyun.admin.domain.query;

import com.manyun.common.core.web.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("规定时间统计商品条件查询对象")
public class StatisticalGoodsQuery extends PageQuery {

    @ApiModelProperty("藏品")
    private List<GoodsQuery> collections;

    @ApiModelProperty("盲盒")
    private List<GoodsQuery> boxs;

}
