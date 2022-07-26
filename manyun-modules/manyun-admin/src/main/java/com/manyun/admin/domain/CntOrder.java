package com.manyun.admin.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.manyun.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@ApiModel("订单对象")
@TableName("cnt_order")
public class CntOrder implements Serializable
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("创建人")
    private String createdBy;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private Date createdTime;

    @Excel(name = "更新人")
    @ApiModelProperty("更新人")
    private String updatedBy;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("更新时间")
    private Date updatedTime;

    @ApiModelProperty("购买数量")
    private Long goodsNum;

    @ApiModelProperty("订单号")
    private String orderNo;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("藏品id")
    private String buiId;

    @ApiModelProperty("商品类型;0藏品，1盲盒")
    private Long goodsType;

    @ApiModelProperty("订单状态;0待付款，1已完成，2已取消，-1支付未回调 3=进行中(这个比较特殊 属于寄售的时候用的)")
    private Long orderStatus;

    @ApiModelProperty("支付方式;0平台，1支付宝，2微信，3银联")
    private Long payType;

    @ApiModelProperty("订单金额")
    private BigDecimal orderAmount;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("付款截止时间")
    private Date endTime;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("付款时间")
    private Date payTime;

    @ApiModelProperty("藏品名称")
    private String collectionName;

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
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
    public void setGoodsNum(Long goodsNum)
    {
        this.goodsNum = goodsNum;
    }

    public Long getGoodsNum()
    {
        return goodsNum;
    }
    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }

    public String getOrderNo()
    {
        return orderNo;
    }
    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getUserId()
    {
        return userId;
    }
    public void setBuiId(String buiId)
    {
        this.buiId = buiId;
    }

    public String getBuiId()
    {
        return buiId;
    }
    public void setGoodsType(Long goodsType)
    {
        this.goodsType = goodsType;
    }

    public Long getGoodsType()
    {
        return goodsType;
    }
    public void setOrderStatus(Long orderStatus)
    {
        this.orderStatus = orderStatus;
    }

    public Long getOrderStatus()
    {
        return orderStatus;
    }
    public void setPayType(Long payType)
    {
        this.payType = payType;
    }

    public Long getPayType()
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
    public void setCollectionName(String collectionName)
    {
        this.collectionName = collectionName;
    }

    public String getCollectionName()
    {
        return collectionName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("createdBy", getCreatedBy())
            .append("createdTime", getCreatedTime())
            .append("updatedBy", getUpdatedBy())
            .append("updatedTime", getUpdatedTime())
            .append("goodsNum", getGoodsNum())
            .append("orderNo", getOrderNo())
            .append("userId", getUserId())
            .append("buiId", getBuiId())
            .append("goodsType", getGoodsType())
            .append("orderStatus", getOrderStatus())
            .append("payType", getPayType())
            .append("orderAmount", getOrderAmount())
            .append("endTime", getEndTime())
            .append("payTime", getPayTime())
            .append("collectionName", getCollectionName())
            .toString();
    }
}
