package com.manyun.admin.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.manyun.common.core.annotation.Excel;
import com.manyun.common.core.web.domain.BaseEntity;

@ApiModel("寄售订单对象")
@TableName("cnt_consignment")
public class CntConsignment implements Serializable
{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("(盲盒|藏品) 名称")
    private String buiName;

    @ApiModelProperty("(盲盒|藏品) 中间表编号")
    private String buiId;

    @ApiModelProperty("系列编号 | 分类编号")
    private String cateId;

    @ApiModelProperty("(盲盒|藏品) 编号")
    private String realBuiId;

    @ApiModelProperty("关联的订单编号")
    private String orderId;

    @ApiModelProperty("0藏品，1盲盒")
    private Integer isType;

    @ApiModelProperty("手续费")
    private BigDecimal serverCharge;

    @ApiModelProperty("寄售价格")
    private BigDecimal consignmentPrice;

    @ApiModelProperty("寄售用户编号")
    private String sendUserId;

    @ApiModelProperty("购买人的用户编号")
    private String payUserId;

    @ApiModelProperty("0待打款,1=已打款")
    private Integer toPay;

    @ApiModelProperty("寄售状态-1=已寄售,2=已锁单(有买方,未支付而已)  3=已售出")
    private Integer consignmentStatus;

    @ApiModelProperty("词条")
    private String formInfo;

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

    public String getBuiName() {
        return buiName;
    }

    public void setBuiName(String buiName) {
        this.buiName = buiName;
    }

    public String getBuiId() {
        return buiId;
    }

    public void setBuiId(String buiId) {
        this.buiId = buiId;
    }

    public String getCateId() {
        return cateId;
    }

    public void setCateId(String cateId) {
        this.cateId = cateId;
    }

    public String getRealBuiId() {
        return realBuiId;
    }

    public void setRealBuiId(String realBuiId) {
        this.realBuiId = realBuiId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getIsType() {
        return isType;
    }

    public void setIsType(Integer isType) {
        this.isType = isType;
    }

    public BigDecimal getServerCharge() {
        return serverCharge;
    }

    public void setServerCharge(BigDecimal serverCharge) {
        this.serverCharge = serverCharge;
    }

    public BigDecimal getConsignmentPrice() {
        return consignmentPrice;
    }

    public void setConsignmentPrice(BigDecimal consignmentPrice) {
        this.consignmentPrice = consignmentPrice;
    }

    public String getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    public String getPayUserId() {
        return payUserId;
    }

    public void setPayUserId(String payUserId) {
        this.payUserId = payUserId;
    }

    public Integer getToPay() {
        return toPay;
    }

    public void setToPay(Integer toPay) {
        this.toPay = toPay;
    }

    public Integer getConsignmentStatus() {
        return consignmentStatus;
    }

    public void setConsignmentStatus(Integer consignmentStatus) {
        this.consignmentStatus = consignmentStatus;
    }

    public String getFormInfo() {
        return formInfo;
    }

    public void setFormInfo(String formInfo) {
        this.formInfo = formInfo;
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
            .append("buiName", getBuiName())
            .append("buiId", getBuiId())
            .append("cateId", getCateId())
            .append("realBuiId", getRealBuiId())
            .append("orderId", getOrderId())
            .append("isType", getIsType())
            .append("serverCharge", getServerCharge())
            .append("consignmentPrice", getConsignmentPrice())
            .append("sendUserId", getSendUserId())
            .append("payUserId", getPayUserId())
            .append("toPay", getToPay())
            .append("consignmentStatus", getConsignmentStatus())
            .append("formInfo", getFormInfo())
            .append("createdBy", getCreatedBy())
            .append("createdTime", getCreatedTime())
            .append("updatedBy", getUpdatedBy())
            .append("updatedTime", getUpdatedTime())
            .toString();
    }
}
