package com.manyun.admin.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.io.Serializable;

@ApiModel("拍卖订单对象")
@TableName("cnt_auction_order")
public class CntAuctionOrder implements Serializable
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("买方id")
    private String toUserId;

    @ApiModelProperty("卖方id")
    private String fromUserId;

    @ApiModelProperty("创建人")
    private String createdBy;

    @ApiModelProperty("创建时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    @ApiModelProperty("更新人")
    private String updatedBy;

    @ApiModelProperty("更新时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;

    @ApiModelProperty("商品类型;1藏品，2盲盒")
    private Integer goodsType;

    @ApiModelProperty("商品id")
    private String goodsId;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("商品图片")
    private String goodsImg;

    @ApiModelProperty("送拍id")
    private String sendAuctionId;

    @ApiModelProperty("出价id")
    private String auctionPriceId;

    @ApiModelProperty("当前价")
    private BigDecimal nowPrice;

    @ApiModelProperty("一口价")
    private BigDecimal soldPrice;

    @ApiModelProperty("起拍价")
    private BigDecimal startPrice;

    @ApiModelProperty("保证金")
    private BigDecimal margin;

    @ApiModelProperty("佣金")
    private BigDecimal commission;

    @ApiModelProperty("拍卖状态;1竞拍中，2待支付，3已支付，4已违约")
    private Integer auctionStatus;

    @ApiModelProperty("订单号")
    private String orderNo;

    @ApiModelProperty("支付方式：0平台，1支付宝，2微信，3银联")
    private Integer payType;

    @ApiModelProperty("订单金额")
    private BigDecimal orderAmount;

    @ApiModelProperty("付款截止时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty("付款时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payTime;

    @ApiModelProperty("我的藏品id")
    private String buiId;

    @ApiModelProperty("组合支付专属,如果此值不是0.00 就说明使用了余额支付")
    private BigDecimal moneyBln;

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }
    public void setToUserId(String toUserId)
    {
        this.toUserId = toUserId;
    }

    public String getToUserId()
    {
        return toUserId;
    }
    public void setFromUserId(String fromUserId)
    {
        this.fromUserId = fromUserId;
    }

    public String getFromUserId()
    {
        return fromUserId;
    }
    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }

    public String getCreatedBy()
    {
        return createdBy;
    }
    public void setCreatedTime(Date createdTime)
    {
        this.createdTime = createdTime;
    }

    public Date getCreatedTime()
    {
        return createdTime;
    }
    public void setUpdatedBy(String updatedBy)
    {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedBy()
    {
        return updatedBy;
    }
    public void setUpdatedTime(Date updatedTime)
    {
        this.updatedTime = updatedTime;
    }

    public Date getUpdatedTime()
    {
        return updatedTime;
    }

    public Integer getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(Integer goodsType) {
        this.goodsType = goodsType;
    }

    public void setGoodsId(String goodsId)
    {
        this.goodsId = goodsId;
    }

    public String getGoodsId()
    {
        return goodsId;
    }
    public void setGoodsName(String goodsName)
    {
        this.goodsName = goodsName;
    }

    public String getGoodsName()
    {
        return goodsName;
    }
    public void setGoodsImg(String goodsImg)
    {
        this.goodsImg = goodsImg;
    }

    public String getGoodsImg()
    {
        return goodsImg;
    }
    public void setSendAuctionId(String sendAuctionId)
    {
        this.sendAuctionId = sendAuctionId;
    }

    public String getSendAuctionId()
    {
        return sendAuctionId;
    }
    public void setAuctionPriceId(String auctionPriceId)
    {
        this.auctionPriceId = auctionPriceId;
    }

    public String getAuctionPriceId()
    {
        return auctionPriceId;
    }
    public void setNowPrice(BigDecimal nowPrice)
    {
        this.nowPrice = nowPrice;
    }

    public BigDecimal getNowPrice()
    {
        return nowPrice;
    }
    public void setSoldPrice(BigDecimal soldPrice)
    {
        this.soldPrice = soldPrice;
    }

    public BigDecimal getSoldPrice()
    {
        return soldPrice;
    }
    public void setStartPrice(BigDecimal startPrice)
    {
        this.startPrice = startPrice;
    }

    public BigDecimal getStartPrice()
    {
        return startPrice;
    }
    public void setMargin(BigDecimal margin)
    {
        this.margin = margin;
    }

    public BigDecimal getMargin()
    {
        return margin;
    }
    public void setCommission(BigDecimal commission)
    {
        this.commission = commission;
    }

    public BigDecimal getCommission()
    {
        return commission;
    }

    public Integer getAuctionStatus() {
        return auctionStatus;
    }

    public void setAuctionStatus(Integer auctionStatus) {
        this.auctionStatus = auctionStatus;
    }

    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }

    public String getOrderNo()
    {
        return orderNo;
    }
    public void setPayType(Integer payType)
    {
        this.payType = payType;
    }

    public Integer getPayType()
    {
        return payType;
    }
    public void setOrderAmount(BigDecimal orderAmount)
    {
        this.orderAmount = orderAmount;
    }

    public BigDecimal getOrderAmount()
    {
        return orderAmount;
    }
    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }
    public void setPayTime(Date payTime)
    {
        this.payTime = payTime;
    }

    public Date getPayTime()
    {
        return payTime;
    }
    public void setBuiId(String buiId)
    {
        this.buiId = buiId;
    }

    public String getBuiId()
    {
        return buiId;
    }
    public void setMoneyBln(BigDecimal moneyBln)
    {
        this.moneyBln = moneyBln;
    }

    public BigDecimal getMoneyBln()
    {
        return moneyBln;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("toUserId", getToUserId())
            .append("fromUserId", getFromUserId())
            .append("createdBy", getCreatedBy())
            .append("createdTime", getCreatedTime())
            .append("updatedBy", getUpdatedBy())
            .append("updatedTime", getUpdatedTime())
            .append("goodsType", getGoodsType())
            .append("goodsId", getGoodsId())
            .append("goodsName", getGoodsName())
            .append("goodsImg", getGoodsImg())
            .append("sendAuctionId", getSendAuctionId())
            .append("auctionPriceId", getAuctionPriceId())
            .append("nowPrice", getNowPrice())
            .append("soldPrice", getSoldPrice())
            .append("startPrice", getStartPrice())
            .append("margin", getMargin())
            .append("commission", getCommission())
            .append("auctionStatus", getAuctionStatus())
            .append("orderNo", getOrderNo())
            .append("payType", getPayType())
            .append("orderAmount", getOrderAmount())
            .append("endTime", getEndTime())
            .append("payTime", getPayTime())
            .append("buiId", getBuiId())
            .append("moneyBln", getMoneyBln())
            .toString();
    }
}
