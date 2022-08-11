package com.manyun.business.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel("用户拥有藏品详细返回视图")
public class UserCollectionForVo implements Serializable {

    @ApiModelProperty("用户用户藏品视图列表Item")
    private UserCollectionVo userCollectionVo;

    // 流转记录
    @ApiModelProperty("流转记录")
    private List<StepVo> stepVos;

    @ApiModelProperty("藏品标签")
    private List<LableVo> lableVos;

    @ApiModelProperty("藏品详情")
    private CollectionInfoVo collectionInfoVo;





}
