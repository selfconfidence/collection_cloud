package com.manyun.business.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel("拍卖藏品详细信息")
@Data
public class AuctionCollectionAllVo implements Serializable {

    @ApiModelProperty("藏品主体信息")
    private CollectionVo collectionVo;

    @ApiModelProperty("藏品介绍信息")
    private CollectionInfoVo collectionInfoVo;

    @ApiModelProperty("藏品拍卖相关信息")
    private AuctionVo auctionVo;
}
