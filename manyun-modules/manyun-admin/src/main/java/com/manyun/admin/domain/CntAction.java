package com.manyun.admin.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@ApiModel("活动对象")
@TableName("cnt_action")
public class CntAction implements Serializable
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("活动标题")
    private String actionTitle;

    @ApiModelProperty("活动封面")
    private String actionImage;

    @ApiModelProperty("活动状态;1=待开始,2=进行中,3=结束")
    private Long actionStatus;

    @ApiModelProperty("活动开始时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty("活动结束时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty("此活动合成的藏品编号")
    private String collectionId;

    @ApiModelProperty("规则内容")
    private String ruleContent;

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
    public void setActionTitle(String actionTitle)
    {
        this.actionTitle = actionTitle;
    }

    public String getActionTitle()
    {
        return actionTitle;
    }
    public void setActionImage(String actionImage)
    {
        this.actionImage = actionImage;
    }

    public String getActionImage()
    {
        return actionImage;
    }
    public void setActionStatus(Long actionStatus)
    {
        this.actionStatus = actionStatus;
    }

    public Long getActionStatus()
    {
        return actionStatus;
    }
    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public Date getStartTime()
    {
        return startTime;
    }
    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }
    public void setCollectionId(String collectionId)
    {
        this.collectionId = collectionId;
    }

    public String getCollectionId()
    {
        return collectionId;
    }
    public void setRuleContent(String ruleContent)
    {
        this.ruleContent = ruleContent;
    }

    public String getRuleContent()
    {
        return ruleContent;
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
            .append("actionTitle", getActionTitle())
            .append("actionImage", getActionImage())
            .append("actionStatus", getActionStatus())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .append("collectionId", getCollectionId())
            .append("ruleContent", getRuleContent())
            .append("createdBy", getCreatedBy())
            .append("createdTime", getCreatedTime())
            .append("updatedBy", getUpdatedBy())
            .append("updatedTime", getUpdatedTime())
            .toString();
    }
}
