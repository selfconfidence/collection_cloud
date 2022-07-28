package com.manyun.admin.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@ApiModel("抽签规则字典返回视图")
@Data
public class DrawRulesDictVo {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("抽签比例(%百分比, 抽中比例)")
    private BigDecimal tarPro;

}
