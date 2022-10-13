package com.manyun.admin.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("统计商品返回视图")
@Data
public class GoodsVo {

        @ApiModelProperty("商品名称")
        private String goodName;

        @ApiModelProperty("商品数量")
        private Integer count;

}
