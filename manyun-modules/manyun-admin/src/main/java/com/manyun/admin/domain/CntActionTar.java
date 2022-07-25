package com.manyun.admin.domain;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@ApiModel("活动合成附属信息对象")
public class CntActionTar implements Serializable
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("活动编号")
    private String actionId;

    @ApiModelProperty("需要多少对应的藏品")
    private Long releaseNum;

    @ApiModelProperty("需要销毁多少藏品 _ 此值不可大于 release_num 字段")
    private Long deleteNum;

    @ApiModelProperty("此目标需要的藏品编号")
    private String collectionId;

    @ApiModelProperty("藏品图片;反三范示")
    private String collectionImage;

    @ApiModelProperty("藏品名称")
    private String collectionName;

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
    public void setReleaseNum(Long releaseNum)
    {
        this.releaseNum = releaseNum;
    }

    public Long getReleaseNum()
    {
        return releaseNum;
    }
    public void setDeleteNum(Long deleteNum)
    {
        this.deleteNum = deleteNum;
    }

    public Long getDeleteNum()
    {
        return deleteNum;
    }
    public void setCollectionId(String collectionId)
    {
        this.collectionId = collectionId;
    }

    public String getCollectionId()
    {
        return collectionId;
    }
    public void setCollectionImage(String collectionImage)
    {
        this.collectionImage = collectionImage;
    }

    public String getCollectionImage()
    {
        return collectionImage;
    }
    public void setCollectionName(String collectionName)
    {
        this.collectionName = collectionName;
    }

    public String getCollectionName()
    {
        return collectionName;
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
            .append("releaseNum", getReleaseNum())
            .append("deleteNum", getDeleteNum())
            .append("collectionId", getCollectionId())
            .append("collectionImage", getCollectionImage())
            .append("collectionName", getCollectionName())
            .append("createdBy", getCreatedBy())
            .append("createdTime", getCreatedTime())
            .append("updatedBy", getUpdatedBy())
            .append("updatedTime", getUpdatedTime())
            .toString();
    }
}
