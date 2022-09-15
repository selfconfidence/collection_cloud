package com.manyun.business.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@Data
@TableName("cnt_user")
@ToString
public class CntUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("认证ID")
    private String certifyId;

    @ApiModelProperty("H5认证专用字段,1=正常,2=已完成,3=失败了")
    private Integer h5RealStatus;

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("激光推送编号 uuid")
    private String jgPush;

    @ApiModelProperty("帐号状态（0正常 1停用）")
    private String status;

    @ApiModelProperty("用户电话号")
    private String phone;

    @ApiModelProperty("区块链地址")
    private String linkAddr;

    @ApiModelProperty("是否实名;1=未实名,2=实名")
    private Integer isReal;

    @ApiModelProperty("邀请码;用户初始化生成")
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

    @ApiModelProperty("创建人")
    private String createdBy;

    @ApiModelProperty("邀请海报")
    private String inviteUrl;

    @ApiModelProperty("创建时间")
    private LocalDateTime createdTime;

    @ApiModelProperty("更新人")
    private String updatedBy;

    @ApiModelProperty("更新时间")
    private LocalDateTime updatedTime;



    public void createD(String createId){
        this.createdBy = createId;
        this.createdTime = LocalDateTime.now();
        if (this.createdTime != null)
            this.updatedTime = this.createdTime;
        this.updatedBy = this.createdBy;
    }

    public void updateD(String updateId){
        this.updatedBy = updateId;
        this.updatedTime = LocalDateTime.now();
    }
}
