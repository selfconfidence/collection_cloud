package com.manyun.admin.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.manyun.common.core.annotation.Excel;
import com.manyun.common.core.web.domain.BaseEntity;


@ApiModel("藏品详情对象")
@TableName("cnt_collection_info")
public class CntCollectionInfo implements Serializable
{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("藏品编号")
    private String collectionId;

    @ApiModelProperty("发行方头像")
    private String publishAuther;

    @ApiModelProperty("发行方简介")
    private String publishInfo;

    @ApiModelProperty("发行方id")
    private String publishId;

    @ApiModelProperty("发行方")
    private String publishOther;

    @ApiModelProperty("购买须知")
    private String customerTail;

    @ApiModelProperty("藏品故事")
    private String lookInfo;

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
    public void setCollectionId(String collectionId)
    {
        this.collectionId = collectionId;
    }

    public String getCollectionId()
    {
        return collectionId;
    }
    public void setPublishAuther(String publishAuther)
    {
        this.publishAuther = publishAuther;
    }

    public String getPublishId() {
        return publishId;
    }

    public void setPublishId(String publishId) {
        this.publishId = publishId;
    }

    public String getPublishAuther()
    {
        return publishAuther;
    }
    public void setPublishOther(String publishOther)
    {
        this.publishOther = publishOther;
    }

    public String getPublishInfo() {
        return publishInfo;
    }

    public void setPublishInfo(String publishInfo) {
        this.publishInfo = publishInfo;
    }

    public String getPublishOther()
    {
        return publishOther;
    }
    public void setCustomerTail(String customerTail)
    {
        this.customerTail = customerTail;
    }

    public String getCustomerTail()
    {
        return customerTail;
    }
    public void setLookInfo(String lookInfo)
    {
        this.lookInfo = lookInfo;
    }

    public String getLookInfo()
    {
        return lookInfo;
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
            .append("collectionId", getCollectionId())
            .append("publishId", getPublishId())
            .append("publishAuther", getPublishAuther())
            .append("publishInfo", getPublishInfo())
            .append("publishOther", getPublishOther())
            .append("customerTail", getCustomerTail())
            .append("lookInfo", getLookInfo())
            .append("createdBy", getCreatedBy())
            .append("createdTime", getCreatedTime())
            .append("updatedBy", getUpdatedBy())
            .append("updatedTime", getUpdatedTime())
            .toString();
    }
}
