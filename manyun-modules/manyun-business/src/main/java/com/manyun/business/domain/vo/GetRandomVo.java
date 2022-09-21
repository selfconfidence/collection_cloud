package com.manyun.business.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("随机因子获取响应参数")
public class GetRandomVo {

    @ApiModelProperty("商户用户唯一编号(用户id)")
    private String userId;

    @ApiModelProperty("随机因子key")
    private String randomKey;

    @ApiModelProperty("随机因子值")
    private String randomValue;

    @ApiModelProperty("license。flag_chnl为ANDROID、IOS、H5时返回")
    private String license;

    @ApiModelProperty("连连RSA公钥")
    private String rsaPublicContent;

}
