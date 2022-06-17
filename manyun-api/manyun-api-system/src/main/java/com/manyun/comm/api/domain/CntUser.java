package com.manyun.comm.api.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
@ApiModel(value = "CntUser对象", description = "用户表")
@Data
public class CntUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("用户电话号")
    private String phone;

    @ApiModelProperty("区块链地址")
    private String linkAddr;

    @ApiModelProperty("是否实名;1=未实名,1=实名")
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

    @ApiModelProperty("创建时间")
    private LocalDateTime createdTime;

    @ApiModelProperty("更新人")
    private String updatedBy;

    @ApiModelProperty("更新时间")
    private LocalDateTime updatedTime;


}
