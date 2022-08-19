package com.manyun.comm.api.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("用户基本信息返回视图")
public class AccountInfoDto implements Serializable {

    @ApiModelProperty("真实姓名")
    private String realName;

    @ApiModelProperty("真实手机号")
    private String realPhone;


}
