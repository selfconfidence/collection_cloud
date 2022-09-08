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

@ApiModel("活动合成目标藏品对象")
@TableName("cnt_action_collection")
public class CntActionCollection implements Serializable
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("活动编号")
    private String actionId;

    @ApiModelProperty("此活动合成的藏品编号")
    private String collectionId;

    @ApiModelProperty("概率 百分比  总和不能超过 100%")
    private BigDecimal tranSvg;

    @ApiModelProperty("库存数量")
    private Integer actionQuantity;

    @ApiModelProperty("已开数量")
    private Integer actionNumber;

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

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }
    public void setActionId(String actionId)
    {
        this.actionId = actionId;
    }

    public String getActionId()
    {
        return actionId;
    }
    public void setCollectionId(String collectionId)
    {
        this.collectionId = collectionId;
    }

    public String getCollectionId()
    {
        return collectionId;
    }

    public BigDecimal getTranSvg() {
        return tranSvg;
    }

    public void setTranSvg(BigDecimal tranSvg) {
        this.tranSvg = tranSvg;
    }

    public void setActionQuantity(Integer actionQuantity)
    {
        this.actionQuantity = actionQuantity;
    }

    public Integer getActionQuantity()
    {
        return actionQuantity;
    }
    public void setActionNumber(Integer actionNumber)
    {
        this.actionNumber = actionNumber;
    }

    public Integer getActionNumber()
    {
        return actionNumber;
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
            .append("actionId", getActionId())
            .append("collectionId", getCollectionId())
            .append("tranSvg", getTranSvg())
            .append("actionQuantity", getActionQuantity())
            .append("actionNumber", getActionNumber())
            .append("createdBy", getCreatedBy())
            .append("createdTime", getCreatedTime())
            .append("updatedBy", getUpdatedBy())
            .append("updatedTime", getUpdatedTime())
            .toString();
    }
}
