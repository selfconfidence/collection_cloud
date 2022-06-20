package com.manyun.business.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 钱包表
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@TableName("cnt_money")
@ApiModel(value = "Money对象", description = "钱包表")
public class Money implements Serializable {

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
        return "Money{" +
        "id=" + id +
        ", userId=" + userId +
        ", realName=" + realName +
        ", realPhone=" + realPhone +
        ", cartJust=" + cartJust +
        ", cartBack=" + cartBack +
        ", moneyBalance=" + moneyBalance +
        ", bankName=" + bankName +
        ", bankCart=" + bankCart +
        ", bankOpen=" + bankOpen +
        ", createdBy=" + createdBy +
        ", createdTime=" + createdTime +
        ", updatedBy=" + updatedBy +
        ", updatedTime=" + updatedTime +
        "}";
    }
}
