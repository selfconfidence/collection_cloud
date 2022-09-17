package com.manyun.admin.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.io.Serializable;

@ApiModel("系统余额提现对象")
@TableName("cnt_system_withdraw")
public class CntSystemWithdraw implements Serializable
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("提现金额")
    private BigDecimal withdrawAmount;

    @ApiModelProperty("剩余余额")
    private BigDecimal moneyBalance;

    @ApiModelProperty("姓名")
    private String userName;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("银行卡")
    private String bankCard;

    @ApiModelProperty("是否已打款（0未打款，1已打款）")
    private Integer withdrawStatus;

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
    public void setWithdrawAmount(BigDecimal withdrawAmount)
    {
        this.withdrawAmount = withdrawAmount;
    }

    public BigDecimal getWithdrawAmount()
    {
        return withdrawAmount;
    }
    public void setMoneyBalance(BigDecimal moneyBalance)
    {
        this.moneyBalance = moneyBalance;
    }

    public BigDecimal getMoneyBalance()
    {
        return moneyBalance;
    }
    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserName()
    {
        return userName;
    }
    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getPhone()
    {
        return phone;
    }
    public void setBankCard(String bankCard)
    {
        this.bankCard = bankCard;
    }

    public String getBankCard()
    {
        return bankCard;
    }
    public void setWithdrawStatus(Integer withdrawStatus)
    {
        this.withdrawStatus = withdrawStatus;
    }

    public Integer getWithdrawStatus()
    {
        return withdrawStatus;
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
            .append("withdrawAmount", getWithdrawAmount())
            .append("moneyBalance", getMoneyBalance())
            .append("userName", getUserName())
            .append("phone", getPhone())
            .append("bankCard", getBankCard())
            .append("withdrawStatus", getWithdrawStatus())
            .append("createdBy", getCreatedBy())
            .append("createdTime", getCreatedTime())
            .append("updatedBy", getUpdatedBy())
            .append("updatedTime", getUpdatedTime())
            .toString();
    }
}
