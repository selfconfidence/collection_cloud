package com.manyun.comm.api.domain.redis.box;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.manyun.comm.api.domain.redis.MediaRedisVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel("盲盒列表返回视图")
public class BoxListRedisVo implements Serializable {

    @ApiModelProperty("盲盒编号")
    private String id;



    @ApiModelProperty("盲盒标题")
    private String boxTitle;

    @ApiModelProperty("已售")
    private Integer selfBalance;

    @ApiModelProperty("库存")
    private Integer balance;

    @ApiModelProperty("盲盒状态;1=正常,2=售罄")
    private Integer statusBy;


    @ApiModelProperty("盲盒现价_实际支付的价格")
    private BigDecimal realPrice;

    @ApiModelProperty("盲盒原价")
    private BigDecimal sourcePrice;



    @ApiModelProperty("盲盒主图")
    private List<MediaRedisVo> mediaVos;

    @ApiModelProperty("盲盒缩略图")
    private List<MediaRedisVo> thumbnailImgMediaVos;

    @ApiModelProperty("盲盒3D图")
    private List<MediaRedisVo> threeDimensionalMediaVos;

    @ApiModelProperty("盲盒详情")
    private String boxInfo;



    @ApiModelProperty("发售时间;到达对应时间点才可以正常交易_平台   yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishTime;


    @ApiModelProperty("创建时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    @ApiModelProperty("是否预售：1预售中，2已开售")
    private Integer preStatus;


}
