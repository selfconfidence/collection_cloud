package com.manyun.comm.api.domain.redis;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("媒体存储器返回视图")
public class MediaRedisVo implements Serializable {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("业务编号")
    private String buiId;

    @ApiModelProperty("模块类型")
    private String modelType;

    @ApiModelProperty("存储地址")
    private String mediaUrl;

    @ApiModelProperty("媒体类型")
    private String mediaType;

}
