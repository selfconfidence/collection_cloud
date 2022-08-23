package com.manyun.admin.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("提前购配置的商品字典")
@Data
public class TqgGoodsDictVo {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("名称")
    private String buiName;

    @ApiModelProperty("商品类型 0=藏品,1=盲盒")
    private Integer isType;

}
