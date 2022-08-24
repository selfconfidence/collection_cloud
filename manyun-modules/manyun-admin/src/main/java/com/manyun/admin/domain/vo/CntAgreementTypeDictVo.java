package com.manyun.admin.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@ApiModel("协议类型字典视图")
@Data
public class CntAgreementTypeDictVo
{

    @ApiModelProperty("协议键值")
    private Integer agreementCode;

    @ApiModelProperty("协议标签")
    private String agreementName;

}
