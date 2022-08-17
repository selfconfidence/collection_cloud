package com.manyun.business.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/*
 * 订单详情视图
 *
 * @author yanwei
 * @create 2022-08-17
 */
@Data
@Api(tags = "订单详情视图")
public class OrderInfoVo implements Serializable {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("创建时间 yyyy-MM-dd HH:mm:ss"  )
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    @ApiModelProperty("订单号")
    private String orderNo;


    @ApiModelProperty("商品类型;0藏品，1盲盒")
    private Integer goodsType;

    @ApiModelProperty("订单状态;0待付款，1已完成，2已取消，-1支付未回调")
    private Integer orderStatus;

    @ApiModelProperty("支付方式;0平台，1支付宝，2微信，3银联")
    private Integer payType;

    @ApiModelProperty("订单金额")
    private BigDecimal orderAmount;

    @ApiModelProperty("付款截止时间 yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @ApiModelProperty("付款时间 yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime payTime;

    @ApiModelProperty("藏品基础信息 goodsType = 0 && (orderStatus = 0 || orderStatus == 2)")
    private CollectionAllVo collectionAllVo;

    @ApiModelProperty("藏品上链后完整信息,goodsType = 0 && orderStatus = 1")
    private UserCollectionForVo userCollectionForVo;

    @ApiModelProperty("盲盒信息,goodsType = 1")
    private BoxVo boxVo;


}