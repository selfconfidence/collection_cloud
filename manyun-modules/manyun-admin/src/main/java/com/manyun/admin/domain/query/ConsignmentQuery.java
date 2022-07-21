package com.manyun.admin.domain.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel("订单管理列表筛选查询视图")
public class ConsignmentQuery
{

    @ApiModelProperty("寄售单号")
    private String id;

    @ApiModelProperty("收藏家手机号")
    private String collectorPhone;

    @ApiModelProperty("收藏家userid")
    private String collectorUserId;

    @ApiModelProperty("收藏家姓名")
    private String collectorName;

    @ApiModelProperty("商品名称")
    private String buiName;

    @ApiModelProperty("0藏品，1盲盒")
    private Long isType;

    @ApiModelProperty("寄售状态-1=已寄售,2=已锁单(有买方,未支付而已)  3=已售出")
    private Long consignmentStatus;

    @ApiModelProperty("卖家姓名")
    private String sellerName;

    @ApiModelProperty("支付方式")
    private Integer payType;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("下单时间")
    private LocalDateTime createOrderTime;

}
