package com.manyun.business.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel("用户拥有藏品返回视图")
public class UserCollectionVo  implements Serializable {

    @ApiModelProperty("用户和藏品唯一标识联系")
    private String id;

    @ApiModelProperty("藏品id")
    private String collectionId;

    @ApiModelProperty("藏品编号;上链后")
    private String collectionNumber;

    @ApiModelProperty("藏品哈希;上链后")
    private String collectionHash;

    @ApiModelProperty("链上地址;上链后")
    private String linkAddr;

    @ApiModelProperty("是否上链;1=未上链,2=已上链")
    private Integer isLink;

    @ApiModelProperty("来源")
    private String sourceInfo;

    @ApiModelProperty("认证机构;上链后")
    private String realCompany;

    @ApiModelProperty("藏品名称")
    private String collectionName;


    @ApiModelProperty("创作者")
    private CnfCreationdVo cnfCreationdVo;


    @ApiModelProperty("系列")
    private CateVo cateVo;

    @ApiModelProperty("藏品主图")
    private List<MediaVo> mediaVos;

    @ApiModelProperty("拥有时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;


}
