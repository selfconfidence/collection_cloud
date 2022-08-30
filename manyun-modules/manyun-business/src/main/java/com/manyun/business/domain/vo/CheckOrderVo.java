package com.manyun.business.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@ApiModel(value = "查询支付结果")
@Data
@ToString
public class CheckOrderVo {

    @ApiModelProperty("订单金额")
    private BigDecimal orderAmount;

    @ApiModelProperty("支付状态，0失败，1成功")
    private Integer payStatus;

    @ApiModelProperty("支付方式0余额支付,4杉德支付 ")
    private Integer payType;

    @ApiModelProperty(" payType = 0 并且 此值不为0.00 ，即为组合支付")
    private BigDecimal moneyBln;

    @ApiModelProperty("支付时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime payTime;
}
