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

@ApiModel("提现配置对象")
@TableName("cnt_withdraw")
public class CntWithdraw implements Serializable
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("提现开关 0:开 1:关")
    private String state;

    @ApiModelProperty("手续费")
    private BigDecimal servicefee;

    @ApiModelProperty("提现金额限制")
    private BigDecimal minmoney;

    @ApiModelProperty("月提现次数")
    private Integer monthlimit;

    @ApiModelProperty("提现规则")
    private String rule;

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public BigDecimal getServicefee() {
        return servicefee;
    }

    public void setServicefee(BigDecimal servicefee) {
        this.servicefee = servicefee;
    }

    public BigDecimal getMinmoney() {
        return minmoney;
    }

    public void setMinmoney(BigDecimal minmoney) {
        this.minmoney = minmoney;
    }

    public Integer getMonthlimit() {
        return monthlimit;
    }

    public void setMonthlimit(Integer monthlimit) {
        this.monthlimit = monthlimit;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
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
            .append("state", getState())
            .append("servicefee", getServicefee())
            .append("minmoney", getMinmoney())
            .append("monthlimit", getMonthlimit())
            .append("rule", getRule())
            .append("createdBy", getCreatedBy())
            .append("createdTime", getCreatedTime())
            .append("updatedBy", getUpdatedBy())
            .append("updatedTime", getUpdatedTime())
            .toString();
    }
}
