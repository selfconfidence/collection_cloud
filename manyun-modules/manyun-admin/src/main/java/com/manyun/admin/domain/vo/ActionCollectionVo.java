package com.manyun.admin.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@ApiModel("活动合成目标藏品对象返回视图")
@Data
public class ActionCollectionVo
{

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("此活动合成的藏品编号")
    private String collectionId;

    @ApiModelProperty("概率 百分比  总和不能超过 100%")
    private BigDecimal tranSvg;

    @ApiModelProperty("库存数量")
    private Integer actionQuantity;

    @ApiModelProperty("已开数量")
    private Integer actionNumber;

}
