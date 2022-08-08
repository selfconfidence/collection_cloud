package com.manyun.business.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel("我的送拍返回视图")
public class MyAuctionSendVo {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("送拍人id")
    private String userId;

    @ApiModelProperty("类型（藏品，盲盒）;1藏品，2盲盒")
    private Integer goodsType;

    @ApiModelProperty("商品id")
    private String goodsId;

    @ApiModelProperty("我的藏品id")
    private String myGoodsId;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("商品图片信息")
    private List<MediaVo> mediaVos;

    @ApiModelProperty("起拍价")
    private BigDecimal startPrice;

    @ApiModelProperty("一口价")
    private BigDecimal soldPrice;

    @ApiModelProperty("当前价")
    private BigDecimal nowPrice;

    @ApiModelProperty("保证金")
    private BigDecimal margin;

    @ApiModelProperty("佣金比例")
    private BigDecimal commission;

    @ApiModelProperty("延拍时长")
    private Integer delayTime;

    @ApiModelProperty("开始时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @ApiModelProperty("结束时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @ApiModelProperty("剩余支付时间，只有状态为待支付才有效")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endPayTime;

    @ApiModelProperty("拍卖状态;1待开始，2竞拍中，3待支付，4已完成，5已违约，6已流拍")
    private Integer auctionSendStatus;

    @ApiModelProperty("创建时间 yyyy-MM-dd HH:mm:ss"  )
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    @ApiModelProperty("更新时间 yyyy-MM-dd HH:mm:ss"  )
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedTime;
}
