package com.manyun.admin.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.manyun.common.core.web.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@ApiModel("拍卖订单对象返回视图")
@Data
public class CntAuctionOrderVo
{

    @ApiModelProperty("订单号")
    private String orderNo;

    @ApiModelProperty("送拍id")
    private String auctionSendId;

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

    @ApiModelProperty("商品图片")
    private String goodsImg;

    @ApiModelProperty("保证金")
    private BigDecimal margin;

    @ApiModelProperty("佣金")
    private BigDecimal commission;

    @ApiModelProperty("起拍价")
    private BigDecimal startPrice;

    @ApiModelProperty("当前价")
    private BigDecimal nowPrice;

    @ApiModelProperty("拍卖状态;1竞拍中，2待支付，3已支付，4已违约")
    private Integer auctionStatus;

    @ApiModelProperty("支付方式：0平台，1支付宝，2微信，3银联")
    private Integer payType;

    @ApiModelProperty("订单金额")
    private BigDecimal orderAmount;

    @ApiModelProperty("付款时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payTime;

    @ApiModelProperty("付款截止时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty("创建时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    @ApiModelProperty("更新时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;

}
