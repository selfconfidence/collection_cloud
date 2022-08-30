package com.manyun.business.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@ApiModel(value = "查询支付结果")
@Data
@ToString
public class CheckOrderVo {

    @ApiModelProperty("订单金额")
    private BigDecimal orderAmount;

    @ApiModelProperty("支付状态，0失败，1成功")
    private Integer payStatus;

    @ApiModelProperty("支付方式")
    private Integer payType;

    @ApiModelProperty("此值不为0，即为组合支付")
    private BigDecimal moneyBln;

}
