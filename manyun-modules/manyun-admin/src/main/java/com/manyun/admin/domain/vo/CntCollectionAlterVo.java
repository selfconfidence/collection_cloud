package com.manyun.admin.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@ApiModel("藏品改动接收参数视图")
@Data
public class CntCollectionAlterVo
{

    @ApiModelProperty("藏品id 增加的时候不需要  修改的时候需要")
    private String id;

    @ApiModelProperty("系列编号")
    private String cateId;

    @ApiModelProperty("创作者编号")
    private String bindCreation;

    @ApiModelProperty("藏品名称")
    private String collectionName;

    @ApiModelProperty("藏品原价")
    private BigDecimal sourcePrice;

    @ApiModelProperty("藏品现价")
    private BigDecimal realPrice;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("发售时间")
    private Date publishTime;

    @ApiModelProperty("发售数量")
    private Long balance;

    @ApiModelProperty("流通数量")
    private Long selfBalance;

}
