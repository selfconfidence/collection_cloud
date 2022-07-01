package com.manyun.business.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel("竞拍列表返回视图")
public class AuctionPriceVo {

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("用户头像")
    private String headImage;

    @ApiModelProperty("出价")
    private BigDecimal bidPrice;

    @ApiModelProperty("创建时间 yyyy-MM-dd HH:mm:ss"  )
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;
}
