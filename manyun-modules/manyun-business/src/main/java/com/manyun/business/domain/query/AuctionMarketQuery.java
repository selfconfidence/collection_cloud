package com.manyun.business.domain.query;

import com.manyun.common.core.web.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("拍卖市场列表查询条件")
public class AuctionMarketQuery extends PageQuery {

    @ApiModelProperty("商品类型：1藏品，2盲盒")
    private Integer goodsType;

}
