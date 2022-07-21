package com.manyun.admin.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.manyun.common.core.annotation.Excel;
import com.manyun.common.core.web.domain.BaseEntity;

@ApiModel("寄售订单对象")
public class CntConsignment extends BaseEntity
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
    private Long isType;

    @ApiModelProperty("手续费")
    private BigDecimal serverCharge;

    @ApiModelProperty("寄售价格")
    private BigDecimal consignmentPrice;

    @ApiModelProperty("寄售用户编号")
    private String sendUserId;

    @ApiModelProperty("购买人的用户编号")
    private String payUserId;

    @ApiModelProperty("0待打款,1=已打款")
    private Long toPay;

    @ApiModelProperty("寄售状态-1=已寄售,2=已锁单(有买方,未支付而已)  3=已售出")
    private Long consignmentStatus;

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

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }
    public void setBuiName(String buiName)
    {
        this.buiName = buiName;
    }

    public String getBuiName()
    {
        return buiName;
    }
    public void setBuiId(String buiId)
    {
        this.buiId = buiId;
    }

    public String getBuiId()
    {
        return buiId;
    }
    public void setCateId(String cateId)
    {
        this.cateId = cateId;
    }

    public String getCateId()
    {
        return cateId;
    }
    public void setRealBuiId(String realBuiId)
    {
        this.realBuiId = realBuiId;
    }

    public String getRealBuiId()
    {
        return realBuiId;
    }
    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }

    public String getOrderId()
    {
        return orderId;
    }
    public void setIsType(Long isType)
    {
        this.isType = isType;
    }

    public Long getIsType()
    {
        return isType;
    }
    public void setServerCharge(BigDecimal serverCharge)
    {
        this.serverCharge = serverCharge;
    }

    public BigDecimal getServerCharge()
    {
        return serverCharge;
    }
    public void setConsignmentPrice(BigDecimal consignmentPrice)
    {
        this.consignmentPrice = consignmentPrice;
    }

    public BigDecimal getConsignmentPrice()
    {
        return consignmentPrice;
    }
    public void setSendUserId(String sendUserId)
    {
        this.sendUserId = sendUserId;
    }

    public String getSendUserId()
    {
        return sendUserId;
    }
    public void setPayUserId(String payUserId)
    {
        this.payUserId = payUserId;
    }

    public String getPayUserId()
    {
        return payUserId;
    }
    public void setToPay(Long toPay)
    {
        this.toPay = toPay;
    }

    public Long getToPay()
    {
        return toPay;
    }
    public void setConsignmentStatus(Long consignmentStatus)
    {
        this.consignmentStatus = consignmentStatus;
    }

    public Long getConsignmentStatus()
    {
        return consignmentStatus;
    }
    public void setFormInfo(String formInfo)
    {
        this.formInfo = formInfo;
    }

    public String getFormInfo()
    {
        return formInfo;
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
