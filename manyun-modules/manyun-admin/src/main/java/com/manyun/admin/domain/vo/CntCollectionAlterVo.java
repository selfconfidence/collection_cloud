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

    @ApiModelProperty("藏品id 增加时删除该参数不需要  修改时传入该参数")
    private String id;

    @ApiModelProperty("系列编号")
    private String cateId;

    @ApiModelProperty("抽签编号")
    private String tarId;

    @ApiModelProperty("创作者编号")
    private String bindCreation;

    @ApiModelProperty("藏品名称")
    private String collectionName;

    @ApiModelProperty("藏品原价")
    private BigDecimal sourcePrice;

    @ApiModelProperty("藏品状态;0=下架,1=正常")
    private Integer statusBy;

    @ApiModelProperty("藏品现价")
    private BigDecimal realPrice;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("发售时间")
    private Date publishTime;

    @ApiModelProperty("发售数量")
    private Integer balance;

    @ApiModelProperty("限定数量")
    private Integer limitNumber;

    @ApiModelProperty("可提前购的时间")
    private Integer postTime;

    @ApiModelProperty("是否推送寄售市场(0=可以，1=不可以)")
    private Integer pushConsignment;

}
