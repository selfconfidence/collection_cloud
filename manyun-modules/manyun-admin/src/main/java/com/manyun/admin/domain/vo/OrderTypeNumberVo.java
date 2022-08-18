package com.manyun.admin.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("订单统计各种类型数量返回视图")
@Data
public class OrderTypeNumberVo {

    @ApiModelProperty("总数量")
    private Integer totalOrders;

    @ApiModelProperty("类型分类统计集合")
    private List<OrderTypePercentageVo> orderTypePercentageList;

}
