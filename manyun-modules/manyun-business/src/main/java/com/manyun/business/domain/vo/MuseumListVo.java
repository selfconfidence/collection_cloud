package com.manyun.business.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("展馆藏品返回视图")
public class MuseumListVo implements Serializable {

    @ApiModelProperty("我的藏品编号")
    private String myGoodsId;

    @ApiModelProperty("藏品编号")
    private String goodsId;

    @ApiModelProperty("图片地址")
    private String mediaUrl;
}
