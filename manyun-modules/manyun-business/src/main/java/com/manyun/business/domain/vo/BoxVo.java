package com.manyun.business.domain.vo;

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

    // 是否需要抽签 才能购买? 1=已抽签,2=未抽中
    @ApiModelProperty("抽签购买状态 0不需要抽签,1=已抽中,2=未抽中,3=未抽签")
    private Integer tarStatus;

    // 该用户是否能提前购?
    @ApiModelProperty("提前购（分钟为单位 （发售时间 - postTime(分钟)) = 可以购买） 如果为null就按发售时间即可; 如果到了发售时间,此字段失效,无需理睬;")
    private Integer postTime;

}
