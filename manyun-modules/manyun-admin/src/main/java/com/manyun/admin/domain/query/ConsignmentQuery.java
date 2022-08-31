package com.manyun.admin.domain.query;

import com.manyun.common.core.web.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel("订单管理条件查询对象")
public class ConsignmentQuery extends PageQuery
{

    @ApiModelProperty("寄售单号")
    private String id;

    @ApiModelProperty("寄售商品名称")
    private String buiName;

    @ApiModelProperty("寄售状态-1=已寄售,2=已锁单(有买方,未支付而已)  3=已售出")
    private Integer consignmentStatus;

    @ApiModelProperty("卖家姓名")
    private String collectorName;

    @ApiModelProperty("卖家手机号")
    private String collectorPhone;

    @ApiModelProperty("买家姓名")
    private String sellerName;

    @ApiModelProperty("买家手机号")
    private String sellerPhone;

    @ApiModelProperty("支付方式")
    private Integer payType;

    @ApiModelProperty("打款状况")
    private Integer toPay;

}
