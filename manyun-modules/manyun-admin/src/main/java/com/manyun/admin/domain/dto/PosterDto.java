package com.manyun.admin.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("活动海报")
@Data
public class PosterDto {

    @ApiModelProperty("规则值")
    private String systemVal;

}
