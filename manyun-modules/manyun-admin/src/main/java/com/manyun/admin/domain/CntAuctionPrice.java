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

@ApiModel("竞价对象")
@TableName("cnt_auction_price")
public class CntAuctionPrice implements Serializable
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("送拍id")
    private String auctionSendId;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("出价")
    private BigDecimal bidPrice;

    @ApiModelProperty("出价状态")
    private Integer auctionStatus;

    @ApiModelProperty("1.最新，2旧")
    private Integer isNew;

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

    @ApiModelProperty("剩余支付时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endPayTime;

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }
    public void setAuctionSendId(String auctionSendId)
    {
        this.auctionSendId = auctionSendId;
    }

    public String getAuctionSendId()
    {
        return auctionSendId;
    }
    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getUserId()
    {
        return userId;
    }
    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserName()
    {
        return userName;
    }
    public void setBidPrice(BigDecimal bidPrice)
    {
        this.bidPrice = bidPrice;
    }

    public BigDecimal getBidPrice()
    {
        return bidPrice;
    }
    public void setAuctionStatus(Integer auctionStatus)
    {
        this.auctionStatus = auctionStatus;
    }

    public Integer getAuctionStatus()
    {
        return auctionStatus;
    }
    public void setIsNew(Integer isNew)
    {
        this.isNew = isNew;
    }

    public Integer getIsNew()
    {
        return isNew;
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
    public void setEndPayTime(Date endPayTime)
    {
        this.endPayTime = endPayTime;
    }

    public Date getEndPayTime()
    {
        return endPayTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("auctionSendId", getAuctionSendId())
            .append("userId", getUserId())
            .append("userName", getUserName())
            .append("bidPrice", getBidPrice())
            .append("auctionStatus", getAuctionStatus())
            .append("isNew", getIsNew())
            .append("createdBy", getCreatedBy())
            .append("createdTime", getCreatedTime())
            .append("updatedBy", getUpdatedBy())
            .append("updatedTime", getUpdatedTime())
            .append("endPayTime", getEndPayTime())
            .toString();
    }
}
