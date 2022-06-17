package com.manyun.business.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 盲盒与藏品中间表
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@TableName("cnt_box_collection")
@ApiModel(value = "BoxCollection对象", description = "盲盒与藏品中间表")
public class BoxCollection implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("盲盒编号")
    private String boxId;

    @ApiModelProperty("藏品编号")
    private String collectionId;

    @ApiModelProperty("藏品名称;反三范式")
    private String collectionName;

    @ApiModelProperty("藏品原价;反三范式")
    private BigDecimal sourcePrice;

    @ApiModelProperty("概率 百分比;box_id 总和不能超过 100%")
    private BigDecimal tranSvg;

    @ApiModelProperty("评分等级;SSR SR 等等")
    private String flagScore;

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

    public String getBoxId() {
        return boxId;
    }

    public void setBoxId(String boxId) {
        this.boxId = boxId;
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

    public BigDecimal getSourcePrice() {
        return sourcePrice;
    }

    public void setSourcePrice(BigDecimal sourcePrice) {
        this.sourcePrice = sourcePrice;
    }

    public BigDecimal getTranSvg() {
        return tranSvg;
    }

    public void setTranSvg(BigDecimal tranSvg) {
        this.tranSvg = tranSvg;
    }

    public String getFlagScore() {
        return flagScore;
    }

    public void setFlagScore(String flagScore) {
        this.flagScore = flagScore;
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
        return "BoxCollection{" +
        "id=" + id +
        ", boxId=" + boxId +
        ", collectionId=" + collectionId +
        ", collectionName=" + collectionName +
        ", sourcePrice=" + sourcePrice +
        ", tranSvg=" + tranSvg +
        ", flagScore=" + flagScore +
        ", createdBy=" + createdBy +
        ", createdTime=" + createdTime +
        ", updatedBy=" + updatedBy +
        ", updatedTime=" + updatedTime +
        "}";
    }
}
