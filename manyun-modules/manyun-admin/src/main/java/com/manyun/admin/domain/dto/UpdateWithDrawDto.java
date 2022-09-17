package com.manyun.admin.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("修改状态参数")
public class UpdateWithDrawDto {

    @ApiModelProperty("id")
    private String id;

}
