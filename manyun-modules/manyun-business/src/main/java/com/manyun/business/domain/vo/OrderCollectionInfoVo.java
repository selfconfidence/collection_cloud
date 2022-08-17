package com.manyun.business.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/*
 * 订单详情中的藏品详情
 *
 * @author yanwei
 * @create 2022-08-17
 */
@Data
@ApiModel("订单中藏品详情信息")
public class OrderCollectionInfoVo implements Serializable {
    @ApiModelProperty("藏品主体信息")
    private CollectionVo collectionVo;

    @ApiModelProperty("藏品介绍信息")
    private CollectionInfoVo collectionInfoVo;


    @ApiModelProperty("(未成功购买)用户用户藏品视图列表Item = null 不展示")
    private UserCollectionVo userCollectionVo;

    // 流转记录
    @ApiModelProperty("(没有流转记录)流转记录 size = 0 不展示")
    private List<StepVo> stepVos;

}
