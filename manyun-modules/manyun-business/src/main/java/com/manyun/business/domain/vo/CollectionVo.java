package com.manyun.business.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel("藏品返回视图")
@Builder
public class CollectionVo implements Serializable {

    @ApiModelProperty("藏品编号")
    private String id;

    @ApiModelProperty("藏品名称")
    private String collectionName;

    @ApiModelProperty("藏品主图")
    private List<MediaVo> mediaVos;

    @ApiModelProperty("系列")
    private CateVo cateVo;

    @ApiModelProperty("创作者")
    private CnfCreationdVo cnfCreationdVo;

    @ApiModelProperty("藏品原价")
    private BigDecimal sourcePrice;

    @ApiModelProperty("已售数量")
    private Integer selfBalance;

    @ApiModelProperty("库存数量")
    private Integer balance;

    @ApiModelProperty("藏品现价_实际支付的价格")
    private BigDecimal realPrice;
    @ApiModelProperty("藏品标签")
    private List<LableVo> lableVos;

    @ApiModelProperty("发售时间;到达对应时间点才可以正常交易_平台   yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishTime;


    @ApiModelProperty("创建时间 yyyy-MM-dd HH:mm:ss"  )
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;


}
