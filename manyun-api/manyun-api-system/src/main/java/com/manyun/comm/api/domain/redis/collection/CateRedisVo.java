package com.manyun.comm.api.domain.redis.collection;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.manyun.comm.api.domain.redis.CreationdRedisVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 藏品系列_分类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@ApiModel(value = "系列相关返回视图", description = "藏品系列_分类")
@Data
@ToString
public class CateRedisVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("夫节点编号")
    private String parentId;


    @ApiModelProperty("系列名称")
    private String cateName;

    @ApiModelProperty("系列主图")
    private String cateImage;

    @ApiModelProperty("系列详情")
    private String cateInfo;

    @ApiModelProperty("创作者编号;当前系列的创作者编号; null 代表没有")
    private String bindCreation;


    @ApiModelProperty("创作者")
    private CreationdRedisVo creationdRedisVo;


    @ApiModelProperty("创建时间 yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;



}
