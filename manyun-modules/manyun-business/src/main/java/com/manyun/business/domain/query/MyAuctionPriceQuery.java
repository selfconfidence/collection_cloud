package com.manyun.business.domain.query;

import com.manyun.common.core.web.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("我的出价查询条件")
@Data
public class MyAuctionPriceQuery extends PageQuery {

    @ApiModelProperty(value = "竞拍状态", notes = "1竞拍中，2未拍中，3待支付，4已支付，5已违约")
    private Integer AuctionPriceStatus;

}
