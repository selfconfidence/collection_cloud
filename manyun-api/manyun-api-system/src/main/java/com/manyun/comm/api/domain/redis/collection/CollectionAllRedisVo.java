package com.manyun.comm.api.domain.redis.collection;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel("藏品详细信息")
@Data
public class CollectionAllRedisVo implements Serializable {

    @ApiModelProperty("藏品主体信息")
    private CollectionRedisVo collectionVo;

    @ApiModelProperty("藏品介绍信息")
    private CollectionInfoRedisVo collectionInfoVo;

}
