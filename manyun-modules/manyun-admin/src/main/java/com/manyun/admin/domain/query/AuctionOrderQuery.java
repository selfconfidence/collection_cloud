package com.manyun.admin.domain.query;

import com.manyun.common.core.web.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@ApiModel("拍卖订单条件查询")
@Data
public class AuctionOrderQuery extends PageQuery
{

    @ApiModelProperty("订单号")
    private String orderNo;

    @ApiModelProperty("卖方昵称")
    private String fromUserName;

    @ApiModelProperty("卖方手机号")
    private String fromUserPhone;

    @ApiModelProperty("买方昵称")
    private String toUserName;

    @ApiModelProperty("买方手机号")
    private String toUserPhone;

    @ApiModelProperty("商品类型;1藏品，2盲盒")
    private Integer goodsType;

    @ApiModelProperty("商品名称")
    private String goodsName;


}
