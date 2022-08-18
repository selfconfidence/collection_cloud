package com.manyun.admin.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("有效订单近七日新增数量统计返回视图")
@Data
public class ValidOrderAddStatisticsVo {

    @ApiModelProperty("日期")
    private String date;

    @ApiModelProperty("数量")
    private Integer count;

}
