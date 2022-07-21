package com.manyun.admin.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.manyun.common.core.annotation.Excel;
import com.manyun.common.core.web.domain.BaseEntity;

@ApiModel("藏品对象")
public class CntCollection extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("藏品名称")
    private String collectionName;

    @ApiModelProperty("抽签编号")
    private String tarId;

    @ApiModelProperty("藏品原价")
    private BigDecimal sourcePrice;

    @ApiModelProperty("实际支付的价格")
    private BigDecimal realPrice;

    @ApiModelProperty("已售")
    private Long selfBalance;

    @ApiModelProperty("库存")
    private Long balance;

    @ApiModelProperty("版本标识")
    private Long versionFlag;

    @ApiModelProperty("藏品状态;0=下架,1=正常,2=售罄")
    private Long statusBy;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("发售时间")
    private Date publishTime;

    @ApiModelProperty("创作者编号")
    private String bindCreation;

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

    @ApiModelProperty("系列编号")
    private String cateId;

    @ApiModelProperty("可以提前购的盲盒子")
    private Long postTime;

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }
    public void setCollectionName(String collectionName)
    {
        this.collectionName = collectionName;
    }

    public String getCollectionName()
    {
        return collectionName;
    }
    public void setTarId(String tarId)
    {
        this.tarId = tarId;
    }

    public String getTarId()
    {
        return tarId;
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
    public void setVersionFlag(Long versionFlag)
    {
        this.versionFlag = versionFlag;
    }

    public Long getVersionFlag()
    {
        return versionFlag;
    }
    public void setStatusBy(Long statusBy)
    {
        this.statusBy = statusBy;
    }

    public Long getStatusBy()
    {
        return statusBy;
    }
    public void setPublishTime(Date publishTime)
    {
        this.publishTime = publishTime;
    }

    public Date getPublishTime()
    {
        return publishTime;
    }
    public void setBindCreation(String bindCreation)
    {
        this.bindCreation = bindCreation;
    }

    public String getBindCreation()
    {
        return bindCreation;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("collectionName", getCollectionName())
            .append("tarId", getTarId())
            .append("sourcePrice", getSourcePrice())
            .append("realPrice", getRealPrice())
            .append("selfBalance", getSelfBalance())
            .append("balance", getBalance())
            .append("versionFlag", getVersionFlag())
            .append("statusBy", getStatusBy())
            .append("publishTime", getPublishTime())
            .append("bindCreation", getBindCreation())
            .append("createdBy", getCreatedBy())
            .append("createdTime", getCreatedTime())
            .append("updatedBy", getUpdatedBy())
            .append("updatedTime", getUpdatedTime())
            .append("cateId", getCateId())
            .append("postTime", getPostTime())
            .toString();
    }
}
