package com.manyun.comm.api.domain.redis.box;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel("盲盒的详细信息")
public class BoxRedisVo implements Serializable {

    @ApiModelProperty("盲盒基础信息")
    private BoxListRedisVo boxListRedisVo;

    @ApiModelProperty("盲盒关联的藏品描述列表")
    private List<BoxCollectionJoinRedisVo> boxCollectionJoinRedisVoList;

}
