package com.manyun.business.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("竞品拍卖相关信息")
public class AuctionVo {

    @ApiModelProperty("起拍价")
    private BigDecimal startPrice;

    @ApiModelProperty("一口价")
    private BigDecimal soldPrice;

    @ApiModelProperty("当前价")
    private BigDecimal nowPrice;

    @ApiModelProperty("保证金")
    private BigDecimal margin;

    @ApiModelProperty("佣金")
    private BigDecimal commission;

    @ApiModelProperty("收藏人数")
    private Integer concernedNum;

    @ApiModelProperty("延拍时长")
    private Integer delayTime;

}
