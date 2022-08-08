package com.manyun.business.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("用户返回视图")
public class UserInfoVo  implements Serializable {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("用户电话号")
    private String phone;

    @ApiModelProperty("链地址")
    private String linkAddr;

    @ApiModelProperty("是否实名;1=未实名,2=实名")
    private Integer isReal;

    @ApiModelProperty("邀请码;用户初始化生成")
    private String pleaseCode;

    @ApiModelProperty("个人简介")
    private String userInfo;

    @ApiModelProperty("用户id;平台内部生成,短编号")
    private String userId;

    @ApiModelProperty("用户头像")
    private String headImage;

    @ApiModelProperty("支付密码")
    private String payPass;


}
