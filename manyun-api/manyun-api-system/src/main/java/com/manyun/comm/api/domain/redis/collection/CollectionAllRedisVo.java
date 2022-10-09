package com.manyun.comm.api.domain.redis.collection;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@ApiModel("藏品详细信息")
@Data
public class CollectionAllRedisVo implements Serializable {

    @ApiModelProperty("藏品主体信息")
    private CollectionRedisVo collectionVo;

    @ApiModelProperty("藏品介绍信息")
    private CollectionInfoRedisVo collectionInfoVo;

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
