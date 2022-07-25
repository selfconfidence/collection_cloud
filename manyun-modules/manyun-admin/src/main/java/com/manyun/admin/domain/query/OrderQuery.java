package com.manyun.admin.domain.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@ApiModel("订单条件查询对象")
@Data
public class OrderQuery
{

    @ApiModelProperty("订单号")
    private String orderNo;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("订单状态;0待付款，1已完成，2已取消，-1支付未回调 3=进行中(这个比较特殊 属于寄售的时候用的)")
    private Long orderStatus;

}
