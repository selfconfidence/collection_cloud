package com.manyun.admin.domain;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.manyun.common.core.web.domain.BaseEntity;


@ApiModel("创作者对象")
public class CnfCreationd implements Serializable
{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("创作者头像")
    private String headImage;

    @ApiModelProperty("创作者名称")
    private String creationName;

    @ApiModelProperty("链地址(模拟)")
    private String linkAddr;

    @ApiModelProperty("创作者简介")
    private String creationInfo;

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
    public void setHeadImage(String headImage)
    {
        this.headImage = headImage;
    }

    public String getHeadImage()
    {
        return headImage;
    }
    public void setCreationName(String creationName)
    {
        this.creationName = creationName;
    }

    public String getCreationName()
    {
        return creationName;
    }
    public void setLinkAddr(String linkAddr)
    {
        this.linkAddr = linkAddr;
    }

    public String getLinkAddr()
    {
        return linkAddr;
    }
    public void setCreationInfo(String creationInfo)
    {
        this.creationInfo = creationInfo;
    }

    public String getCreationInfo()
    {
        return creationInfo;
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
            .append("headImage", getHeadImage())
            .append("creationName", getCreationName())
            .append("linkAddr", getLinkAddr())
            .append("creationInfo", getCreationInfo())
            .append("createdBy", getCreatedBy())
            .append("createdTime", getCreatedTime())
            .append("updatedBy", getUpdatedBy())
            .append("updatedTime", getUpdatedTime())
            .toString();
    }
}
