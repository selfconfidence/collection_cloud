package com.manyun.admin.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@ApiModel("用户对象")
@TableName("cnt_user")
public class CntUser implements Serializable
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("用户电话号")
    private String phone;

    @ApiModelProperty("区块链地址")
    private String linkAddr;

    @ApiModelProperty("是否实名;1=未实名,2=实名")
    private Integer isReal;

    @ApiModelProperty(" 邀请码;用户初始化生成")
    private String pleaseCode;

    @ApiModelProperty("父编号")
    private String parentId;

    @ApiModelProperty("个人简介")
    private String userInfo;

    @ApiModelProperty("用户id;平台内部生成,短编号")
    private String userId;

    @ApiModelProperty("用户头像")
    private String headImage;

    @ApiModelProperty("登录密码")
    private String loginPass;

    @ApiModelProperty("支付密码")
    private String payPass;

    @ApiModelProperty("邀请海报地址")
    private String inviteUrl;

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

    @ApiModelProperty("帐号状态（0正常 1停用）")
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLinkAddr() {
        return linkAddr;
    }

    public void setLinkAddr(String linkAddr) {
        this.linkAddr = linkAddr;
    }

    public Integer getIsReal() {
        return isReal;
    }

    public void setIsReal(Integer isReal) {
        this.isReal = isReal;
    }

    public String getPleaseCode() {
        return pleaseCode;
    }

    public void setPleaseCode(String pleaseCode) {
        this.pleaseCode = pleaseCode;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getLoginPass() {
        return loginPass;
    }

    public void setLoginPass(String loginPass) {
        this.loginPass = loginPass;
    }

    public String getPayPass() {
        return payPass;
    }

    public void setPayPass(String payPass) {
        this.payPass = payPass;
    }

    public String getInviteUrl() {
        return inviteUrl;
    }

    public void setInviteUrl(String inviteUrl) {
        this.inviteUrl = inviteUrl;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("nickName", getNickName())
                .append("phone", getPhone())
                .append("linkAddr", getLinkAddr())
                .append("isReal", getIsReal())
                .append("pleaseCode", getPleaseCode())
                .append("parentId", getParentId())
                .append("userInfo", getUserInfo())
                .append("userId", getUserId())
                .append("headImage", getHeadImage())
                .append("loginPass", getLoginPass())
                .append("payPass", getPayPass())
                .append("createdBy", getCreatedBy())
                .append("createdTime", getCreatedTime())
                .append("updatedBy", getUpdatedBy())
                .append("updatedTime", getUpdatedTime())
                .append("status", getStatus())
                .toString();
    }
}
