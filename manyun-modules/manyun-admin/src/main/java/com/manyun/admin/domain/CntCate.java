package com.manyun.admin.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


@ApiModel("藏品系列分类对象")
@TableName("cnt_cate")
public class CntCate implements Serializable
{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("夫级别分类编号_顶层默认为0")
    private String parentId;

    @ApiModelProperty("1=藏品系列，2=盲盒分类")
    private Integer cateType;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Integer getCateType() {
        return cateType;
    }

    public void setCateType(Integer cateType) {
        this.cateType = cateType;
    }

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }

    public String getCateImage() {
        return cateImage;
    }

    public void setCateImage(String cateImage) {
        this.cateImage = cateImage;
    }

    public String getCateInfo() {
        return cateInfo;
    }

    public void setCateInfo(String cateInfo) {
        this.cateInfo = cateInfo;
    }

    public String getBindCreation() {
        return bindCreation;
    }

    public void setBindCreation(String bindCreation) {
        this.bindCreation = bindCreation;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("parentId", getParentId())
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
