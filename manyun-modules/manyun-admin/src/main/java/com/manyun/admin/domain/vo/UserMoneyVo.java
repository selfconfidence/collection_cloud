package com.manyun.admin.domain.vo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


@Data
@ApiModel("用户管理列表返回视图")
public class UserMoneyVo {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("帐号状态（0正常 1停用）")
    private String status;

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("用户电话号")
    private String phone;

    @ApiModelProperty("真实手机号")
    private String realPhone;

    @ApiModelProperty("真实姓名")
    private String realName;

    @ApiModelProperty("身份证正面图片链接")
    private String cartJust;

    @ApiModelProperty("身份证反面图片链接")
    private String cartBack;

    @ApiModelProperty("银行类型")
    private String bankName;

    @ApiModelProperty("账号")
    private String bankCart;

    @ApiModelProperty("开户所在地")
    private String bankOpen;

    @ApiModelProperty("创建时间")
    private Date createdTime;

    @ApiModelProperty("登录时间")
    private Date loginTime;

}
