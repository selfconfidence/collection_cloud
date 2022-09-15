package com.manyun.business.domain.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
@ApiModel(value = "连连提现申请请求参数")
public class LLWithdrawQuery {

    @ApiModelProperty("支付密码")
    @NotBlank(message = "支付密码不能为空!")
    private String passWord;

    @ApiModelProperty("提现金额")
    @NotBlank(message = "提现金额不能为空!")
    private BigDecimal amount;

}
