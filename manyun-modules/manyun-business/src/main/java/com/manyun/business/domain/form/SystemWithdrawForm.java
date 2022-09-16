package com.manyun.business.domain.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("系统余额提现表单")
public class SystemWithdrawForm implements Serializable {

    @ApiModelProperty("提现金额")
    @NotNull
    private BigDecimal withdrawAmount;

    @ApiModelProperty("支付密码")
    @NotNull
    private String payPass;
}
