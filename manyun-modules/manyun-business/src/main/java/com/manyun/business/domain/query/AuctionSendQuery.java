package com.manyun.business.domain.query;

import com.manyun.common.core.web.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("我的送拍查询条件")
public class AuctionSendQuery extends PageQuery {

    @ApiModelProperty("拍卖状态;1待开始，2竞拍中，3待支付，4已完成，5已违约，6已流拍")
    private Integer AuctionSendStatus;

}
