package com.manyun.admin.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.io.Serializable;

@ApiModel("产品举报反馈对象")
@TableName("cnt_feedback")
public class CntFeedback implements Serializable
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("反馈用户id")
    private String feedbackUserId;

    @ApiModelProperty("反馈用户名称")
    private String feedbackUserName;

    @ApiModelProperty("反馈用户手机号")
    private String feedbackUserPhone;

    @ApiModelProperty("区块链地址")
    private String linkAddr;

    @ApiModelProperty("举报类型 1:举报他人炒作/交易藏品   2:举报他人使用外挂等违规手段抢购藏品  3:其他")
    private String feedbackType;

    @ApiModelProperty("反馈内容")
    private String feedbackContent;

    @ApiModelProperty("反馈图片")
    private String feedbackImg;

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

    @ApiModelProperty("处理状态（0:未处理 1:已处理）")
    private String status;

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }
    public void setFeedbackUserId(String feedbackUserId)
    {
        this.feedbackUserId = feedbackUserId;
    }

    public String getFeedbackUserId()
    {
        return feedbackUserId;
    }
    public void setFeedbackUserName(String feedbackUserName)
    {
        this.feedbackUserName = feedbackUserName;
    }

    public String getFeedbackUserName()
    {
        return feedbackUserName;
    }
    public void setFeedbackUserPhone(String feedbackUserPhone)
    {
        this.feedbackUserPhone = feedbackUserPhone;
    }

    public String getFeedbackUserPhone()
    {
        return feedbackUserPhone;
    }
    public void setLinkAddr(String linkAddr)
    {
        this.linkAddr = linkAddr;
    }

    public String getLinkAddr()
    {
        return linkAddr;
    }
    public void setFeedbackType(String feedbackType)
    {
        this.feedbackType = feedbackType;
    }

    public String getFeedbackType()
    {
        return feedbackType;
    }
    public void setFeedbackContent(String feedbackContent)
    {
        this.feedbackContent = feedbackContent;
    }

    public String getFeedbackContent()
    {
        return feedbackContent;
    }
    public void setFeedbackImg(String feedbackImg)
    {
        this.feedbackImg = feedbackImg;
    }

    public String getFeedbackImg()
    {
        return feedbackImg;
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
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("feedbackUserId", getFeedbackUserId())
            .append("feedbackUserName", getFeedbackUserName())
            .append("feedbackUserPhone", getFeedbackUserPhone())
            .append("linkAddr", getLinkAddr())
            .append("feedbackType", getFeedbackType())
            .append("feedbackContent", getFeedbackContent())
            .append("feedbackImg", getFeedbackImg())
            .append("createdBy", getCreatedBy())
            .append("createdTime", getCreatedTime())
            .append("updatedBy", getUpdatedBy())
            .append("updatedTime", getUpdatedTime())
            .append("status", getStatus())
            .toString();
    }
}
