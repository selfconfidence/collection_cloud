package com.manyun.admin.domain.query;

import com.manyun.common.core.web.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("竞拍列表查询条件")
public class AuctionPriceQuery extends PageQuery {

    @ApiModelProperty("送拍id")
    private String auctionSendId;
}
