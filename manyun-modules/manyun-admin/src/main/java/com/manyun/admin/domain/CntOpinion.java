package com.manyun.admin.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.io.Serializable;

@ApiModel("产品建议对象")
@TableName("cnt_opinion")
public class CntOpinion implements Serializable
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("建议用户id")
    private String opinionUserId;

    @ApiModelProperty("建议用户名称")
    private String opinionUserName;

    @ApiModelProperty("建议用户手机号")
    private String opinionUserPhone;

    @ApiModelProperty("建议内容")
    private String opinionContent;

    @ApiModelProperty("图片")
    private String img;

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
    public void setOpinionUserId(String opinionUserId)
    {
        this.opinionUserId = opinionUserId;
    }

    public String getOpinionUserId()
    {
        return opinionUserId;
    }
    public void setOpinionUserName(String opinionUserName)
    {
        this.opinionUserName = opinionUserName;
    }

    public String getOpinionUserName()
    {
        return opinionUserName;
    }
    public void setOpinionUserPhone(String opinionUserPhone)
    {
        this.opinionUserPhone = opinionUserPhone;
    }

    public String getOpinionUserPhone()
    {
        return opinionUserPhone;
    }
    public void setOpinionContent(String opinionContent)
    {
        this.opinionContent = opinionContent;
    }

    public String getOpinionContent()
    {
        return opinionContent;
    }
    public void setImg(String img)
    {
        this.img = img;
    }

    public String getImg()
    {
        return img;
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
            .append("opinionUserId", getOpinionUserId())
            .append("opinionUserName", getOpinionUserName())
            .append("opinionUserPhone", getOpinionUserPhone())
            .append("opinionContent", getOpinionContent())
            .append("img", getImg())
            .append("createdBy", getCreatedBy())
            .append("createdTime", getCreatedTime())
            .append("updatedBy", getUpdatedBy())
            .append("updatedTime", getUpdatedTime())
            .append("status", getStatus())
            .toString();
    }
}
