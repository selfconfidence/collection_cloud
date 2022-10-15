package com.manyun.admin.domain.vo;

import com.manyun.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


@Data
@ApiModel("规定时间藏品交易量及交易金额返回视图")
public class CollectionBusinessVo {

    @ApiModelProperty("用户Id")
    @Excel(name = "用户id",width = 25)
    private String userId;

    @ApiModelProperty("用户电话号")
    @Excel(name = "用户手机号",width = 20)
    private String phone;

    @ApiModelProperty("区块链地址")
    @Excel(name = "区块链地址",width = 80)
    private String linkAddr;

    @ApiModelProperty("交易单量")
    @Excel(name = "交易单量")
    private BigDecimal orderNumber;

    @ApiModelProperty("交易金额")
    @Excel(name = "交易金额")
    private BigDecimal price;

}
