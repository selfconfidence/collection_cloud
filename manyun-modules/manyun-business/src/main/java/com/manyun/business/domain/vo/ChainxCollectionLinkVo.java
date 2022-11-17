package com.manyun.business.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/*
 * 链上查询信息
 *
 * @author yanwei
 * @create 2022-11-10
 */
@Data
@ApiModel("藏品链上查询交易返回视图")
public class ChainxCollectionLinkVo implements Serializable {

    @ApiModelProperty("藏品名称")
    public String collectionName;

    @ApiModelProperty("藏品HASH")
    private String collectionHash;

    @ApiModelProperty("藏品编号;编码")
    private String collectionNumber;

    @ApiModelProperty("发行方")
    private String publishOther;

    @ApiModelProperty("创作者")
    private CnfCreationdVo cnfCreationdVo;

    @ApiModelProperty("发行方简介 = 作品故事")
    private String publishInfo;

    @ApiModelProperty("流转记录")
    private List<StepVo> stepVos;


}
