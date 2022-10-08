package com.manyun.admin.domain.redis.collection;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.manyun.admin.domain.vo.CnfCreationdVo;
import com.manyun.admin.domain.vo.MediaVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel("藏品返回视图")
public class CollectionVo implements Serializable {

    @ApiModelProperty("藏品编号")
    private String id;

    @ApiModelProperty("藏品名称")
    private String collectionName;

    @ApiModelProperty("藏品主图")
    private List<MediaVo> mediaVos;

    @ApiModelProperty("藏品缩略图")
    private List<MediaVo> thumbnailImgMediaVos;

    @ApiModelProperty("藏品3D图")
    private List<MediaVo> threeDimensionalMediaVos;

    @ApiModelProperty("系列")
    private CateVo cateVo;

    @ApiModelProperty("创作者")
    private CnfCreationdVo cnfCreationdVo;

    @ApiModelProperty("藏品原价")
    private BigDecimal sourcePrice;


    @ApiModelProperty("藏品状态;1=正常,2=售罄")
    private Integer statusBy;

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

    @ApiModelProperty("是否预售：1预售中，2已开售")
    private Integer preStatus;


}
