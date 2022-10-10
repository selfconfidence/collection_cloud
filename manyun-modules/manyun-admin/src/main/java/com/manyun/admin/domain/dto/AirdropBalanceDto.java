package com.manyun.admin.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("空投库存占位请求参数")
public class AirdropBalanceDto {

    @ApiModelProperty("商品id")
    @NotBlank(message = "商品id不可以为空")
    private String goodsId;

    @ApiModelProperty("空投库存")
    @NotNull
    @Min(value = 1L,message = "空投库存最小为1")
    private Integer airdropBalance;

}
