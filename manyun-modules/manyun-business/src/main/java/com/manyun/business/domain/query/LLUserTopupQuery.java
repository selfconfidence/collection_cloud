package com.manyun.business.domain.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "充值的请求参数")
public class LLUserTopupQuery {

    @ApiModelProperty("充值金额")
    private BigDecimal amount;

    @ApiModelProperty("表单提交之后跳转回app的地址")
    private String returnUrl;

}
