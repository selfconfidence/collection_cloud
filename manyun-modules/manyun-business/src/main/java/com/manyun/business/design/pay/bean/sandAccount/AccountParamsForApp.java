package com.manyun.business.design.pay.bean.sandAccount;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("杉德开户app所需参数")
public class AccountParamsForApp {

    @ApiModelProperty("商户订单号")
    private String mer_order_no;

    @ApiModelProperty("订单创建时间")
    private String create_time;

    @ApiModelProperty("订单金额")
    private String order_amt;

    @ApiModelProperty("异步通知地址")
    private String notify_url;

    @ApiModelProperty("客户端IP")
    private String create_ip;

    @ApiModelProperty("支付扩展域")
    private String pay_extra;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("用户姓名")
    private String nickName;

    @ApiModelProperty("分账标识")
    private String accsplit_flag;

    @ApiModelProperty("订单失效时间 ")
    private String expire_time;

    @ApiModelProperty("商品名称 ")
    private String goods_name;

    @ApiModelProperty("产品编码")
    private String product_code;


}
