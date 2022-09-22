package com.manyun.business.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "用户系列下藏品返回视图",description = "用户系列下藏品返回视图")
public class UserCateCollectionVo implements Serializable {

    @ApiModelProperty("藏品编号")
    private String id;

    @ApiModelProperty("用户和藏品得唯一编号")
    private String userCollectionId;

    @ApiModelProperty("藏品名称")
    private String collectionName;

    @ApiModelProperty("藏品主图")
    private List<MediaVo> mediaVos;

    @ApiModelProperty("藏品缩略图")
    private List<MediaVo> thumbnailImgMediaVos;

    @ApiModelProperty("藏品3D图")
    private List<MediaVo> threeDimensionalMediaVos;

}
