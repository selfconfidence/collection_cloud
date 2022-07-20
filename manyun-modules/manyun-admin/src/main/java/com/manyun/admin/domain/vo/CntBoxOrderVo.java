package com.manyun.admin.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("盲盒订单返回视图")
public class CntBoxOrderVo {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("订单号")
    private String orderNo;

    @ApiModelProperty("藏品id")
    private String buiId;

    @ApiModelProperty("藏品原价")
    private BigDecimal sourcePrice;

    @ApiModelProperty("实际支付的价格")
    private BigDecimal realPrice;

    @ApiModelProperty("订单金额")
    private BigDecimal orderAmount;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private Date createdTime;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("更新时间")
    private Date updatedTime;

}
