package com.manyun.admin.domain.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@ApiModel("取消寄售订单对象")
@Data
public class CancelConsignmentOrderQuery {

    @ApiModelProperty("卖方用户id")
    @NotBlank(message= "卖方用户id不能为空")
    private String userId;

    @ApiModelProperty("寄售id")
    @NotBlank(message= "寄售id不能为空")
    private String id;

}
