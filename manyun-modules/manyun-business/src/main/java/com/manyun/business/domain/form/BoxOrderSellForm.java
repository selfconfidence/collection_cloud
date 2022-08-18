package com.manyun.business.domain.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel("预先生成订单购买盲盒提交表单")
public class BoxOrderSellForm implements Serializable {

    @ApiModelProperty(value = "盲盒编号",required = true)
    @NotBlank(message = "盲盒编号不可为空")
    private String boxId;


    @ApiModelProperty(value = "购买数量",required = true)
    @NotNull(message = "购买数量不可为空")
    @Range(min = 1,message = "最少1件起售")
    private Integer sellNum;

}
