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

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("提现金额")
    private BigDecimal withdrawAmount;

    @ApiModelProperty("实际到账金额")
    private BigDecimal realWithdrawAmount;

    @ApiModelProperty("剩余余额")
    private BigDecimal moneyBalance;

    @ApiModelProperty("姓名")
    private String userName;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("银行卡")
    private String bankCard;

    @ApiModelProperty("支付宝账户")
    private String aliAccount;

    @ApiModelProperty("提现详情")
    private String withdrawMsg;

    @ApiModelProperty("提现单号")
    private String orderNo;

    @ApiModelProperty("是否已打款（0未打款，1已打款, 2取消打款）")
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

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

    public BigDecimal getRealWithdrawAmount() {
        return realWithdrawAmount;
    }

    public void setRealWithdrawAmount(BigDecimal realWithdrawAmount) {
        this.realWithdrawAmount = realWithdrawAmount;
    }

    public String getAliAccount() {
        return aliAccount;
    }

    public void setAliAccount(String aliAccount) {
        this.aliAccount = aliAccount;
    }

    public String getWithdrawMsg() {
        return withdrawMsg;
    }

    public void setWithdrawMsg(String withdrawMsg) {
        this.withdrawMsg = withdrawMsg;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    @Override
    public String toString() {
        return "CntSystemWithdraw{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", withdrawAmount=" + withdrawAmount +
                ", realWithdrawAmount=" + realWithdrawAmount +
                ", moneyBalance=" + moneyBalance +
                ", userName='" + userName + '\'' +
                ", phone='" + phone + '\'' +
                ", bankCard='" + bankCard + '\'' +
                ", aliAccount='" + aliAccount + '\'' +
                ", withdrawMsg='" + withdrawMsg + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", withdrawStatus=" + withdrawStatus +
                ", createdBy='" + createdBy + '\'' +
                ", createdTime=" + createdTime +
                ", updatedBy='" + updatedBy + '\'' +
                ", updatedTime=" + updatedTime +
                '}';
    }

}
