package com.manyun.admin.domain;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.manyun.common.core.annotation.Excel;
import com.manyun.common.core.web.domain.BaseEntity;


@ApiModel("藏品系列分类对象")
public class CntCate implements Serializable
{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("1=藏品系列，2=盲盒分类")
    private Long cateType;

    @ApiModelProperty("系列名称")
    private String cateName;

    @ApiModelProperty("系列主图")
    private String cateImage;

    @ApiModelProperty("系列详情")
    private String cateInfo;

    @ApiModelProperty("创作者编号;当前系列的创作者编号_可以为空")
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

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }
    public void setCateType(Long cateType)
    {
        this.cateType = cateType;
    }

    public Long getCateType()
    {
        return cateType;
    }
    public void setCateName(String cateName)
    {
        this.cateName = cateName;
    }

    public String getCateName()
    {
        return cateName;
    }
    public void setCateImage(String cateImage)
    {
        this.cateImage = cateImage;
    }

    public String getCateImage()
    {
        return cateImage;
    }
    public void setCateInfo(String cateInfo)
    {
        this.cateInfo = cateInfo;
    }

    public String getCateInfo()
    {
        return cateInfo;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("cateType", getCateType())
            .append("cateName", getCateName())
            .append("cateImage", getCateImage())
            .append("cateInfo", getCateInfo())
            .append("bindCreation", getBindCreation())
            .append("createdBy", getCreatedBy())
            .append("createdTime", getCreatedTime())
            .append("updatedBy", getUpdatedBy())
            .append("updatedTime", getUpdatedTime())
            .toString();
    }
}
