package com.manyun.business.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel("我的出价返回视图，显示竞品信息")
public class MyAuctionPriceVo {

    @ApiModelProperty("送拍id")
    private String auctionSendId;

    @ApiModelProperty("商品id")
    private String goodsId;

    @ApiModelProperty("商品类型：1藏品，2盲盒")
    private Integer goodsType;

    @ApiModelProperty("竞品名称")
    private String goodsName;

    @ApiModelProperty("竞品图片")
    private String goodsImg;

    @ApiModelProperty("竞品拍卖相关信息")
    private AuctionVo auctionVo;

    @ApiModelProperty("竞拍状态")
    private Integer auctionStatus;

    @ApiModelProperty("剩余支付时间，只有状态为待支付才有效")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endPayTime;

}
