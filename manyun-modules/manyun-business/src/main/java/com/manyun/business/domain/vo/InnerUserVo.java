package com.manyun.business.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("开户请求参数")
@Data
public class InnerUserVo {

    @ApiModelProperty("开户之后跳回app的url")
    private String returnUrl;

}
