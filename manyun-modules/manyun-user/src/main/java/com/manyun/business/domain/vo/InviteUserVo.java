package com.manyun.business.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("邀请好友返回视图")
public class InviteUserVo {

    @ApiModelProperty("海报链接")
    private String inviteUrl;

    @ApiModelProperty("邀请码")
    private String inviteCode;

    @ApiModelProperty("注册链接")
    private String regUrl;

}
