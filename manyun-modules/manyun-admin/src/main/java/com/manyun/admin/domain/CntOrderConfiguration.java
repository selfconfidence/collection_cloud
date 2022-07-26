package com.manyun.admin.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@ApiModel("订单配置对象")
@TableName("cnt_order_configuration")
public class CntOrderConfiguration implements Serializable
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("余额支付开关 0:开 1:关")
    private String balancePay;

    @ApiModelProperty("转赠限制")
    private Long makeGive;

    @ApiModelProperty("支付限制")
    private Long payLimitTime;

    @ApiModelProperty("实名认证配置 0:未实名 1:已实名")
    private String identify;

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
    public void setBalancePay(String balancePay)
    {
        this.balancePay = balancePay;
    }

    public String getBalancePay()
    {
        return balancePay;
    }
    public void setMakeGive(Long makeGive)
    {
        this.makeGive = makeGive;
    }

    public Long getMakeGive()
    {
        return makeGive;
    }
    public void setPayLimitTime(Long payLimitTime)
    {
        this.payLimitTime = payLimitTime;
    }

    public Long getPayLimitTime()
    {
        return payLimitTime;
    }
    public void setIdentify(String identify)
    {
        this.identify = identify;
    }

    public String getIdentify()
    {
        return identify;
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
            .append("balancePay", getBalancePay())
            .append("makeGive", getMakeGive())
            .append("payLimitTime", getPayLimitTime())
            .append("identify", getIdentify())
            .append("createdBy", getCreatedBy())
            .append("createdTime", getCreatedTime())
            .append("updatedBy", getUpdatedBy())
            .append("updatedTime", getUpdatedTime())
            .toString();
    }
}
