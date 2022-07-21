package com.manyun.admin.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("盲盒中的物品对象视图返回")
public class CntBoxCollectionVo
{

    @ApiModelProperty("藏品编号")
    private String collectionId;

    @ApiModelProperty("概率 百分比;box_id 总和不能超过 100%")
    private BigDecimal tranSvg;

    @ApiModelProperty("评分等级;SSR SR 等等")
    private String flagScore;

    @ApiModelProperty("藏品原价")
    private BigDecimal sourcePrice;

}
