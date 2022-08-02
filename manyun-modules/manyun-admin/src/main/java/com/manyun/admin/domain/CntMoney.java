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

@ApiModel("钱包对象")
@TableName("cnt_money")
public class CntMoney implements Serializable
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("用户编号")
    private String userId;

    @ApiModelProperty("真实姓名")
    private String realName;

    @ApiModelProperty("真实手机号")
    private String realPhone;

    @ApiModelProperty("身份证正面_图片链接")
    private String cartJust;

    @ApiModelProperty("身份证反面_图片链接")
    private String cartBack;

    @ApiModelProperty("是否绑定银行卡  1=绑定,2=未绑定")
    private Integer isBindCart;

    @ApiModelProperty("钱包余量")
    private BigDecimal moneyBalance;

    @ApiModelProperty("银行卡名称;中国银行,工商银行等")
    private String bankName;

    @ApiModelProperty("银行卡号")
    private String bankCart;

    @ApiModelProperty("银行开户行")
    private String bankOpen;

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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRealPhone() {
        return realPhone;
    }

    public void setRealPhone(String realPhone) {
        this.realPhone = realPhone;
    }

    public String getCartJust() {
        return cartJust;
    }

    public void setCartJust(String cartJust) {
        this.cartJust = cartJust;
    }

    public String getCartBack() {
        return cartBack;
    }

    public void setCartBack(String cartBack) {
        this.cartBack = cartBack;
    }

    public Integer getIsBindCart() {
        return isBindCart;
    }

    public void setIsBindCart(Integer isBindCart) {
        this.isBindCart = isBindCart;
    }

    public BigDecimal getMoneyBalance() {
        return moneyBalance;
    }

    public void setMoneyBalance(BigDecimal moneyBalance) {
        this.moneyBalance = moneyBalance;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCart() {
        return bankCart;
    }

    public void setBankCart(String bankCart) {
        this.bankCart = bankCart;
    }

    public String getBankOpen() {
        return bankOpen;
    }

    public void setBankOpen(String bankOpen) {
        this.bankOpen = bankOpen;
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
            .append("realName", getRealName())
            .append("realPhone", getRealPhone())
            .append("cartJust", getCartJust())
            .append("cartBack", getCartBack())
            .append("isBindCart", getIsBindCart())
            .append("moneyBalance", getMoneyBalance())
            .append("bankName", getBankName())
            .append("bankCart", getBankCart())
            .append("bankOpen", getBankOpen())
            .append("createdBy", getCreatedBy())
            .append("createdTime", getCreatedTime())
            .append("updatedBy", getUpdatedBy())
            .append("updatedTime", getUpdatedTime())
            .toString();
    }
}
