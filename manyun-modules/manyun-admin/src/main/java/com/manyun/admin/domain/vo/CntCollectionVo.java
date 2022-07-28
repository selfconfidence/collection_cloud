package com.manyun.admin.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel("藏品管理对象返回视图")
public class CntCollectionVo {

    @ApiModelProperty("藏品状态;0=下架,1=正常,2=售罄")
    private Integer statusBy;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("藏品名称")
    private String collectionName;

    @ApiModelProperty("藏品主图")
    private List<MediaVo> mediaVos;

    @ApiModelProperty("藏品原价")
    private BigDecimal sourcePrice;

    @ApiModelProperty("实际支付的价格")
    private BigDecimal realPrice;

    @ApiModelProperty("库存")
    private Integer balance;

    @ApiModelProperty("已售")
    private Integer selfBalance;

}
