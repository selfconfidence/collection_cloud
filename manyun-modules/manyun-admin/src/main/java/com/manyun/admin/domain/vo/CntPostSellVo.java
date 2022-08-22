package com.manyun.admin.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("提前购配置可以购买对象视图")
@Data
public class CntPostSellVo
{

    @ApiModelProperty("商品名称")
    private String buiId;

    @ApiModelProperty("商品类型 0=藏品,1=盲盒")
    private Integer isType;

    @ApiModelProperty("购买次数")
    private Integer buyFrequency;

}
