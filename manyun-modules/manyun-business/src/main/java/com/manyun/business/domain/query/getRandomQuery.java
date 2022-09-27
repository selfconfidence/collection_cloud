package com.manyun.business.domain.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("随机因子请求参数")
public class getRandomQuery {

    @ApiModelProperty(value = "交易发起渠道 ANDROID IOS H5 PCH5 PC",required = true)
    @NotBlank(message = "交易发起渠道不能为空!")
    private String flagChnl;

    @ApiModelProperty(value = "APP包名。flag_chnl为H5时，送商户一级域名")
    private String pkgName;

    @ApiModelProperty(value = "APP应用名。flag_chnl为H5时，送商户一级域名")
    private String appName;

}
