package com.manyun.admin.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.manyun.common.core.annotation.Excel;
import com.manyun.common.core.web.domain.BaseEntity;

@ApiModel("盲盒与藏品中间对象")
public class CntBoxCollection implements Serializable
{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("盲盒编号")
    private String boxId;

    @ApiModelProperty("藏品编号")
    private String collectionId;

    @ApiModelProperty("藏品名称")
    private String collectionName;

    @ApiModelProperty("藏品原价")
    private BigDecimal sourcePrice;

    @ApiModelProperty("概率 百分比;box_id 总和不能超过 100%")
    private BigDecimal tranSvg;

    @ApiModelProperty("评分等级;SSR SR 等等")
    private String flagScore;

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
    public void setBoxId(String boxId)
    {
        this.boxId = boxId;
    }

    public String getBoxId()
    {
        return boxId;
    }
    public void setCollectionId(String collectionId)
    {
        this.collectionId = collectionId;
    }

    public String getCollectionId()
    {
        return collectionId;
    }
    public void setCollectionName(String collectionName)
    {
        this.collectionName = collectionName;
    }

    public String getCollectionName()
    {
        return collectionName;
    }
    public void setSourcePrice(BigDecimal sourcePrice)
    {
        this.sourcePrice = sourcePrice;
    }

    public BigDecimal getSourcePrice()
    {
        return sourcePrice;
    }
    public void setTranSvg(BigDecimal tranSvg)
    {
        this.tranSvg = tranSvg;
    }

    public BigDecimal getTranSvg()
    {
        return tranSvg;
    }
    public void setFlagScore(String flagScore)
    {
        this.flagScore = flagScore;
    }

    public String getFlagScore()
    {
        return flagScore;
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
            .append("boxId", getBoxId())
            .append("collectionId", getCollectionId())
            .append("collectionName", getCollectionName())
            .append("sourcePrice", getSourcePrice())
            .append("tranSvg", getTranSvg())
            .append("flagScore", getFlagScore())
            .append("createdBy", getCreatedBy())
            .append("createdTime", getCreatedTime())
            .append("updatedBy", getUpdatedBy())
            .append("updatedTime", getUpdatedTime())
            .toString();
    }
}
