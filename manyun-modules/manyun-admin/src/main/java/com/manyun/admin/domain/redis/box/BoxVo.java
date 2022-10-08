package com.manyun.admin.domain.redis.box;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel("盲盒的详细信息")
public class BoxVo implements Serializable {

    @ApiModelProperty("盲盒基础信息")
    private BoxListVo boxListVo;

    @ApiModelProperty("盲盒关联的藏品描述列表")
    private List<BoxCollectionJoinVo> boxCollectionJoinVos;

}
