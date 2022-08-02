package com.manyun.admin.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel("订单管理列表返回视图")
public class CntConsignmentVo
{

    @ApiModelProperty("寄售单号")
    private String id;

    @ApiModelProperty("0藏品，1盲盒")
    private Integer isType;

    @ApiModelProperty("寄售商品名称")
    private String buiName;

    @ApiModelProperty("寄售价格")
    private BigDecimal consignmentPrice;

    @ApiModelProperty("手续费")
    private BigDecimal serverCharge;

    @ApiModelProperty("寄售状态-1=已寄售,2=已锁单(有买方,未支付而已)  3=已售出")
    private Integer consignmentStatus;

    @ApiModelProperty("打款状况 0待打款,1=已打款")
    private Integer toPay;

    @ApiModelProperty("卖家姓名")
    private String collectorName;

    @ApiModelProperty("卖家手机号")
    private String collectorPhone;

    @ApiModelProperty("买家姓名")
    private String sellerName;

    @ApiModelProperty("买家手机号")
    private String sellerPhone;

    @ApiModelProperty("支付方式 0平台，1支付宝，2微信，3银联")
    private Integer payType;

    @ApiModelProperty("订单金额")
    private BigDecimal orderAmount;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("下单时间")
    private LocalDateTime createOrderTime;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("付款时间")
    private LocalDateTime payTime;

}
