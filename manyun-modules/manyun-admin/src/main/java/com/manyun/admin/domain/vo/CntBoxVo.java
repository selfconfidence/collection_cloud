package com.manyun.admin.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("盲盒列表返回视图")
public class CntBoxVo {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("盲盒标题")
    private String boxTitle;

    @ApiModelProperty("盲盒主图")
    private List<MediaVo> mediaVos;

    @ApiModelProperty("藏品原价")
    private BigDecimal sourcePrice;

    @ApiModelProperty("实际支付的价格")
    private BigDecimal realPrice;

    @ApiModelProperty("盲盒状态;0=下架,1=正常,2=售罄")
    private Integer statusBy;

    @ApiModelProperty("库存")
    private Integer balance;

    @ApiModelProperty("已售")
    private Integer selfBalance;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("发售时间")
    private Date publishTime;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private Date createdTime;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("更新时间")
    private Date updatedTime;

    @ApiModelProperty("总量")
    private Integer totalBalance;

    @ApiModelProperty("是否推送寄售市场(0=可以，1=不可以)")
    private Integer pushConsignment;

}
