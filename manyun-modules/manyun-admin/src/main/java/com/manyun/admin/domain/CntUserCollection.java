package com.manyun.admin.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.io.Serializable;

@ApiModel("用户购买藏品中间对象")
@TableName("cnt_user_collection")
public class CntUserCollection implements Serializable
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("用户编号")
    private String userId;

    @ApiModelProperty("藏品id")
    private String collectionId;

    @ApiModelProperty("藏品名称")
    private String collectionName;

    @ApiModelProperty("藏品编号;购买后")
    private String collectionNumber;

    @ApiModelProperty("藏品哈希;购买后")
    private String collectionHash;

    @ApiModelProperty("链上地址;购买后")
    private String linkAddr;

    @ApiModelProperty("是否上链;1=未上链,2=已上链")
    private Integer isLink;

    @ApiModelProperty("来源")
    private String sourceInfo;

    @ApiModelProperty("1=存在,2=不存在(寄售回滚有用)")
    private Integer isExist;

    @ApiModelProperty("认证机构;购买后")
    private String realCompany;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public String getCollectionNumber() {
        return collectionNumber;
    }

    public void setCollectionNumber(String collectionNumber) {
        this.collectionNumber = collectionNumber;
    }

    public String getCollectionHash() {
        return collectionHash;
    }

    public void setCollectionHash(String collectionHash) {
        this.collectionHash = collectionHash;
    }

    public String getLinkAddr() {
        return linkAddr;
    }

    public void setLinkAddr(String linkAddr) {
        this.linkAddr = linkAddr;
    }

    public Integer getIsLink() {
        return isLink;
    }

    public void setIsLink(Integer isLink) {
        this.isLink = isLink;
    }

    public String getSourceInfo() {
        return sourceInfo;
    }

    public void setSourceInfo(String sourceInfo) {
        this.sourceInfo = sourceInfo;
    }

    public Integer getIsExist() {
        return isExist;
    }

    public void setIsExist(Integer isExist) {
        this.isExist = isExist;
    }

    public String getRealCompany() {
        return realCompany;
    }

    public void setRealCompany(String realCompany) {
        this.realCompany = realCompany;
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
            .append("userId", getUserId())
            .append("collectionId", getCollectionId())
            .append("collectionName", getCollectionName())
            .append("collectionNumber", getCollectionNumber())
            .append("collectionHash", getCollectionHash())
            .append("linkAddr", getLinkAddr())
            .append("isLink", getIsLink())
            .append("sourceInfo", getSourceInfo())
            .append("isExist", getIsExist())
            .append("realCompany", getRealCompany())
            .append("createdBy", getCreatedBy())
            .append("createdTime", getCreatedTime())
            .append("updatedBy", getUpdatedBy())
            .append("updatedTime", getUpdatedTime())
            .toString();
    }
}
