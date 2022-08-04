package com.manyun.business.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    @ApiModelProperty("加价幅度")
    private BigDecimal auctionPriceRange;

    @ApiModelProperty("开始时间 yyyy-MM-dd HH:mm:ss"  )
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @ApiModelProperty("结束时间 yyyy-MM-dd HH:mm:ss"  )
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @ApiModelProperty("拍卖状态拍卖状态;1待开始，2竞拍中，3待支付，4已完成，5已违约，6已流拍")
    private Integer auctionSendStatus;

    @ApiModelProperty("剩余支付时间:当状态为3时有用")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endPayTime;

}
