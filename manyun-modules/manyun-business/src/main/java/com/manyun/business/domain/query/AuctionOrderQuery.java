package com.manyun.business.domain.query;

import com.manyun.common.core.web.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("拍卖订单查询条件")
public class AuctionOrderQuery extends PageQuery {

    @ApiModelProperty("拍卖状态")
    private Integer auctionStatus;
}
