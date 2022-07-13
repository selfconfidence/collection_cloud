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

    @ApiModelProperty("价格排序查询 0=倒序,1=正序(默认倒序)")
    private Integer priceOrder = 0;

    @ApiModelProperty("搜索标题")
    private String commName;

    @ApiModelProperty("系列编号")
    private String cateId;

}
