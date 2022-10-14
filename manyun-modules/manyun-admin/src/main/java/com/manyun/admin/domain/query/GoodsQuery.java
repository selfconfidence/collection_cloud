package com.manyun.admin.domain.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("统计商品条件查询")
@Data
public class GoodsQuery {

        @ApiModelProperty("商品数量")
        private Integer count;

        @ApiModelProperty("商品id")
        private String goodId;

}
