package com.manyun.business.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@ApiModel("藏品详细信息")
public class CollectionAllVo {

    @ApiModelProperty("藏品主体信息")
    private CollectionVo collectionVo;

    @ApiModelProperty("藏品介绍信息")
    private CollectionInfoVo collectionInfoVo;


}
