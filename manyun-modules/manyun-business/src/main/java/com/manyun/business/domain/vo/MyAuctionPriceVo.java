package com.manyun.business.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("我的出价返回视图，显示竞品信息")
public class MyAuctionPriceVo {

    @ApiModelProperty("竞品id")
    private String auctionSendId;

    @ApiModelProperty("竞品名称")
    private String goodsName;

    @ApiModelProperty("竞品图片")
    private String goodsImg;

    @ApiModelProperty("竞品拍卖相关信息")
    private AuctionVo auctionVo;

}
