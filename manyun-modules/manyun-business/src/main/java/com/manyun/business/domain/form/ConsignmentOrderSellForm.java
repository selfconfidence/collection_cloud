package com.manyun.business.domain.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel("预先生成订单购买寄售市场资产提交表单")
public class ConsignmentOrderSellForm implements Serializable {

    @ApiModelProperty(value = "寄售编号",required = true)
    @NotBlank(message = "寄售编号不可为空")
    private String buiId;




}
