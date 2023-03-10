package com.manyun.business.domain.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("系统余额提现表单")
public class SystemWithdrawForm implements Serializable {

    @ApiModelProperty("提现金额")
    @NotNull
    @Min(value = 1L,message = "提现最低一元起!")
    private BigDecimal withdrawAmount;

    @ApiModelProperty("支付宝账号")
    @NotBlank
    private String aliAccount;

    @ApiModelProperty("支付密码")
    @NotNull
    private String payPass;
}
