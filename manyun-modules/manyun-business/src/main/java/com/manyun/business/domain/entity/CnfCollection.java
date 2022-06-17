package com.manyun.business.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 藏品表
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@TableName("cnt_collection")
@ApiModel(value = "Collection对象", description = "藏品表")
public class CnfCollection implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("藏品名称")
    private String collectionName;

    @ApiModelProperty("藏品原价")
    private BigDecimal sourcePrice;

    @ApiModelProperty("藏品现价_实际支付的价格")
    private BigDecimal realPrice;

    @ApiModelProperty("藏品编号;购买后")
    private String collectionNumber;

    @ApiModelProperty("藏品哈希;购买后")
    private String collectionHash;

    @ApiModelProperty("链上地址;购买后")
    private String linkAddr;

    @ApiModelProperty("认证机构;购买后")
    private String realCompany;

    @ApiModelProperty("是否上链;1=未上链,2=已上链")
    private Integer isLink;

    @ApiModelProperty("已售")
    private Integer selfBalance;

    @ApiModelProperty("库存")
    private Integer balance;

    @ApiModelProperty("版本标识")
    private Integer versionFlag;

    @ApiModelProperty("藏品状态;0=下架,1=正常,2=售罄")
    private Integer statusBy;

    @ApiModelProperty("发售时间;到达对应时间点才可以正常交易_平台")
    private LocalDateTime publishTime;

    @ApiModelProperty("创作者编号;当前系列的创作者编号")
    private String bindCreation;

    @ApiModelProperty("创建人")
    private String createdBy;

    @ApiModelProperty("创建时间")
    private LocalDateTime createdTime;

    @ApiModelProperty("更新人")
    private String updatedBy;

    @ApiModelProperty("更新时间")
    private LocalDateTime updatedTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public BigDecimal getSourcePrice() {
        return sourcePrice;
    }

    public void setSourcePrice(BigDecimal sourcePrice) {
        this.sourcePrice = sourcePrice;
    }

    public BigDecimal getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(BigDecimal realPrice) {
        this.realPrice = realPrice;
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

    public String getRealCompany() {
        return realCompany;
    }

    public void setRealCompany(String realCompany) {
        this.realCompany = realCompany;
    }

    public Integer getIsLink() {
        return isLink;
    }

    public void setIsLink(Integer isLink) {
        this.isLink = isLink;
    }

    public Integer getSelfBalance() {
        return selfBalance;
    }

    public void setSelfBalance(Integer selfBalance) {
        this.selfBalance = selfBalance;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public Integer getVersionFlag() {
        return versionFlag;
    }

    public void setVersionFlag(Integer versionFlag) {
        this.versionFlag = versionFlag;
    }

    public Integer getStatusBy() {
        return statusBy;
    }

    public void setStatusBy(Integer statusBy) {
        this.statusBy = statusBy;
    }

    public LocalDateTime getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(LocalDateTime publishTime) {
        this.publishTime = publishTime;
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

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    @Override
    public String toString() {
        return "Collection{" +
        "id=" + id +
        ", collectionName=" + collectionName +
        ", sourcePrice=" + sourcePrice +
        ", realPrice=" + realPrice +
        ", collectionNumber=" + collectionNumber +
        ", collectionHash=" + collectionHash +
        ", linkAddr=" + linkAddr +
        ", realCompany=" + realCompany +
        ", isLink=" + isLink +
        ", selfBalance=" + selfBalance +
        ", balance=" + balance +
        ", versionFlag=" + versionFlag +
        ", statusBy=" + statusBy +
        ", publishTime=" + publishTime +
        ", bindCreation=" + bindCreation +
        ", createdBy=" + createdBy +
        ", createdTime=" + createdTime +
        ", updatedBy=" + updatedBy +
        ", updatedTime=" + updatedTime +
        "}";
    }
}
