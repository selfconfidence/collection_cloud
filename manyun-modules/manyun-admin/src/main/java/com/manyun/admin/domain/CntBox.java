package com.manyun.admin.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.manyun.common.core.annotation.Excel;
import com.manyun.common.core.web.domain.BaseEntity;


@ApiModel("盲盒对象")
public class CntBox extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("分类编号")
    private String cateId;

    @ApiModelProperty("可以提前购的盲盒子 - 如果为null 就代表不是提前购")
    private Long postTime;

    @ApiModelProperty("抽签编号 如果为null不需要抽签,否则需要抽签")
    private String tarId;

    @ApiModelProperty("盲盒标题")
    private String boxTitle;

    @ApiModelProperty("已售")
    private Long selfBalance;

    @ApiModelProperty("库存")
    private Long balance;

    @ApiModelProperty("1未开启,2已开启")
    private Long boxOpen;

    @ApiModelProperty("盲盒状态;0=下架,1=正常,2=售罄 ")
    private Long statusBy;

    @ApiModelProperty("盲盒详情")
    private String boxInfo;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("发售时间")
    private Date publishTime;

    @ApiModelProperty("藏品原价")
    private BigDecimal sourcePrice;

    @ApiModelProperty("藏品实际支付的价格")
    private BigDecimal realPrice;

    @ApiModelProperty("创建人")
    private String createdBy;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private Date createdTime;

    @ApiModelProperty("更新人")
    private String updatedBy;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("更新时间")
    private Date updatedTime;

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }
    public void setCateId(String cateId)
    {
        this.cateId = cateId;
    }

    public String getCateId()
    {
        return cateId;
    }
    public void setPostTime(Long postTime)
    {
        this.postTime = postTime;
    }

    public Long getPostTime()
    {
        return postTime;
    }
    public void setTarId(String tarId)
    {
        this.tarId = tarId;
    }

    public String getTarId()
    {
        return tarId;
    }
    public void setBoxTitle(String boxTitle)
    {
        this.boxTitle = boxTitle;
    }

    public String getBoxTitle()
    {
        return boxTitle;
    }
    public void setSelfBalance(Long selfBalance)
    {
        this.selfBalance = selfBalance;
    }

    public Long getSelfBalance()
    {
        return selfBalance;
    }
    public void setBalance(Long balance)
    {
        this.balance = balance;
    }

    public Long getBalance()
    {
        return balance;
    }
    public void setBoxOpen(Long boxOpen)
    {
        this.boxOpen = boxOpen;
    }

    public Long getBoxOpen()
    {
        return boxOpen;
    }
    public void setStatusBy(Long statusBy)
    {
        this.statusBy = statusBy;
    }

    public Long getStatusBy()
    {
        return statusBy;
    }
    public void setBoxInfo(String boxInfo)
    {
        this.boxInfo = boxInfo;
    }

    public String getBoxInfo()
    {
        return boxInfo;
    }
    public void setPublishTime(Date publishTime)
    {
        this.publishTime = publishTime;
    }

    public Date getPublishTime()
    {
        return publishTime;
    }
    public void setSourcePrice(BigDecimal sourcePrice)
    {
        this.sourcePrice = sourcePrice;
    }

    public BigDecimal getSourcePrice()
    {
        return sourcePrice;
    }
    public void setRealPrice(BigDecimal realPrice)
    {
        this.realPrice = realPrice;
    }

    public BigDecimal getRealPrice()
    {
        return realPrice;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("cateId", getCateId())
            .append("postTime", getPostTime())
            .append("tarId", getTarId())
            .append("boxTitle", getBoxTitle())
            .append("selfBalance", getSelfBalance())
            .append("balance", getBalance())
            .append("boxOpen", getBoxOpen())
            .append("statusBy", getStatusBy())
            .append("boxInfo", getBoxInfo())
            .append("publishTime", getPublishTime())
            .append("sourcePrice", getSourcePrice())
            .append("realPrice", getRealPrice())
            .append("createdBy", getCreatedBy())
            .append("createdTime", getCreatedTime())
            .append("updatedBy", getUpdatedBy())
            .append("updatedTime", getUpdatedTime())
            .toString();
    }
}
