package com.manyun.admin.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("订单各种状态占订单总量比例")
@Data
public class OrderTypePercentageVo {

    @ApiModelProperty("订单状态;0待付款，1已完成，2已取消，-1支付未回调 3=进行中(这个比较特殊 属于寄售的时候用的)")
    private Integer orderStatus;

    @ApiModelProperty("类型占比数量")
    private Integer totalTypePercentages;

}
