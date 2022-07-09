package com.manyun.business.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel("拍卖盲盒详细信息")
@Data
public class AuctionBoxAllVo implements Serializable {

    @ApiModelProperty("盲盒基础信息")
    private BoxListVo boxListVo;

    @ApiModelProperty("盲盒关联的藏品描述列表")
    private List<BoxCollectionJoinVo> boxCollectionJoinVos;

    @ApiModelProperty("盲盒拍卖相关信息")
    private AuctionVo auctionVo;
}
