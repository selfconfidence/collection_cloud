package com.manyun.admin.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@ApiModel("藏品改动接收参数视图")
@Data
public class CntCollectionDetailsVo
{

    @ApiModelProperty("藏品id")
    private String id;

    @ApiModelProperty("系列编号")
    private String cateId;

    @ApiModelProperty("藏品名称")
    private String collectionName;

    @ApiModelProperty("藏品原价")
    private BigDecimal sourcePrice;

    @ApiModelProperty("藏品现价")
    private BigDecimal realPrice;

    @ApiModelProperty("可提前购的时间")
    private Integer postTime;

    @ApiModelProperty("藏品状态;0=下架,1=正常")
    private Integer statusBy;

    @ApiModelProperty("抽签编号")
    private String tarId;

    @ApiModelProperty("创作者编号")
    private String bindCreation;

    @ApiModelProperty("发行方")
    private String issuanceId;

    @ApiModelProperty("标签编号")
    private List<String> lableIds;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("发售时间")
    private Date publishTime;

    @ApiModelProperty("发售数量")
    private Integer balance;

    @ApiModelProperty("流通数量")
    private Integer selfBalance;

    @ApiModelProperty("主图")
    private List<MediaVo> mediaVos;

    @ApiModelProperty("购买须知")
    private String customerTail;

    @ApiModelProperty("藏品故事")
    private String lookInfo;

}
