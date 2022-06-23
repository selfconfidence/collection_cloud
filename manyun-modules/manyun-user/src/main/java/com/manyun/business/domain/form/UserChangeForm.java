package com.manyun.business.domain.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("用户修改个人信息")
public class UserChangeForm implements Serializable {


    @ApiModelProperty("用户头像")
    private String headImage;

    @ApiModelProperty("个人简介")
    private String userInfo;


    @ApiModelProperty("用户昵称")
    private String nickName;
}
