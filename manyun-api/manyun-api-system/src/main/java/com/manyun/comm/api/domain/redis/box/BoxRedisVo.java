package com.manyun.comm.api.domain.redis.box;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel("盲盒的详细信息")
public class BoxRedisVo implements Serializable {

    @ApiModelProperty("盲盒基础信息")
    private BoxListRedisVo boxListVo;

    @ApiModelProperty("盲盒关联的藏品描述列表")
    private List<BoxCollectionJoinRedisVo> boxCollectionJoinVos;


    // 是否需要抽签 才能购买? 1=已抽签,2=未抽中
    @ApiModelProperty("抽签购买状态 0不需要抽签,1=已抽中,2=未抽中,3=未抽签，4=等待开奖")
    private transient Integer tarStatus;

    @ApiModelProperty("抽签结果公布时间,tarStatus = 4 有效")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private transient LocalDateTime openTime;

    // 该用户是否能提前购?
    @ApiModelProperty("提前购（分钟为单位 （发售时间 - postTime(分钟)) = 可以购买） 如果为null就按发售时间即可; 如果到了发售时间,此字段失效,无需理睬;")
    private transient Integer postTime;
}
