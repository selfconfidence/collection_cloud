package com.manyun.admin.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@ApiModel("抽签规则(盲盒,藏品)返回视图")
@Data
public class CntTarVo
{

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("抽签比例(%百分比, 抽中比例)")
    private BigDecimal tarPro;

    @ApiModelProperty("抽签类型;(1=盲盒,2=藏品)")
    private Long tarType;

    @ApiModelProperty("创建时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    @ApiModelProperty("更新时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;

}
