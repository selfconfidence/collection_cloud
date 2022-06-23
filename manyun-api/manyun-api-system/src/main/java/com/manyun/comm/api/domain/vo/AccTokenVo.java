package com.manyun.comm.api.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("登录返回token")
@Builder
public class AccTokenVo implements Serializable {

    @ApiModelProperty("token 码")
    private String access_token;

    @ApiModelProperty("过期时间(分钟为单位)")
    private Long expires_in;



}
