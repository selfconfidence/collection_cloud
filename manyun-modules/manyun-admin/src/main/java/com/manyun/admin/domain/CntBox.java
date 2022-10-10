package com.manyun.admin.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


@ApiModel("盲盒对象")
@TableName("cnt_box")
public class CntBox implements Serializable
{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("分类编号")
    private String cateId;

    @TableField(updateStrategy = FieldStrategy.IGNORED )
    @ApiModelProperty("可以提前购的盲盒子 - 如果为null 就代表不是提前购")
    private Integer postTime;

    @TableField(updateStrategy = FieldStrategy.IGNORED )
    @ApiModelProperty("抽签编号 如果为null不需要抽签,否则需要抽签")
    private String tarId;

    @ApiModelProperty("盲盒标题")
    private String boxTitle;

    @ApiModelProperty("已售")
    private Integer selfBalance;

    @ApiModelProperty("库存")
    private Integer balance;

    @ApiModelProperty("空投已售")
    private Integer airdropSelfBalance;

    @ApiModelProperty("空投库存")
    private Integer airdropBalance;

    @ApiModelProperty("1未开启,2已开启")
    private Integer boxOpen;

    @ApiModelProperty("盲盒状态;0=下架,1=正常,2=售罄 ")
    private Integer statusBy;

    @ApiModelProperty("盲盒详情")
    private String boxInfo;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("发售时间")
    private Date publishTime;

    @ApiModelProperty("藏品原价")
    private BigDecimal sourcePrice;

    @ApiModelProperty("藏品实际支付的价格")
    private BigDecimal realPrice;

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

    @ApiModelProperty("是否推送寄售市场(0=可以，1=不可以)")
    private Integer pushConsignment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCateId() {
        return cateId;
    }

    public void setCateId(String cateId) {
        this.cateId = cateId;
    }

    public Integer getPostTime() {
        return postTime;
    }

    public void setPostTime(Integer postTime) {
        this.postTime = postTime;
    }

    public String getTarId() {
        return tarId;
    }

    public void setTarId(String tarId) {
        this.tarId = tarId;
    }

    public String getBoxTitle() {
        return boxTitle;
    }

    public void setBoxTitle(String boxTitle) {
        this.boxTitle = boxTitle;
    }

    public Integer getSelfBalance() {
        return selfBalance;
    }

    public void setSelfBalance(Integer selfBalance) {
        this.selfBalance = selfBalance;
    }

    public Integer getAirdropSelfBalance() {
        return airdropSelfBalance;
    }

    public void setAirdropSelfBalance(Integer airdropSelfBalance) {
        this.airdropSelfBalance = airdropSelfBalance;
    }

    public Integer getAirdropBalance() {
        return airdropBalance;
    }

    public void setAirdropBalance(Integer airdropBalance) {
        this.airdropBalance = airdropBalance;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public Integer getBoxOpen() {
        return boxOpen;
    }

    public void setBoxOpen(Integer boxOpen) {
        this.boxOpen = boxOpen;
    }

    public Integer getStatusBy() {
        return statusBy;
    }

    public void setStatusBy(Integer statusBy) {
        this.statusBy = statusBy;
    }

    public String getBoxInfo() {
        return boxInfo;
    }

    public void setBoxInfo(String boxInfo) {
        this.boxInfo = boxInfo;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
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

    public Integer getPushConsignment() {
        return pushConsignment;
    }

    public void setPushConsignment(Integer pushConsignment) {
        this.pushConsignment = pushConsignment;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("cateId", getCateId())
            .append("postTime", getPostTime())
            .append("tarId", getTarId())
            .append("boxTitle", getBoxTitle())
            .append("selfBalance", getSelfBalance())
            .append("balance", getBalance())
            .append("boxOpen", getBoxOpen())
            .append("statusBy", getStatusBy())
            .append("boxInfo", getBoxInfo())
            .append("publishTime", getPublishTime())
            .append("sourcePrice", getSourcePrice())
            .append("realPrice", getRealPrice())
            .append("createdBy", getCreatedBy())
            .append("createdTime", getCreatedTime())
            .append("updatedBy", getUpdatedBy())
            .append("updatedTime", getUpdatedTime())
            .append("pushConsignment", getPushConsignment())
            .toString();
    }
}
