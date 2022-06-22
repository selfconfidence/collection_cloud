package com.manyun.business.domain.query;

import com.manyun.common.core.web.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("订单相关查询条件")
public class OrderQuery extends PageQuery {

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("订单状态;0待付款，1已完成，2已取消，-1支付未回调")
    private Integer orderStatus;
}
