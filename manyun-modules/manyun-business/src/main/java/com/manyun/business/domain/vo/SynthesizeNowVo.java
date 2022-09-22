package com.manyun.business.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class SynthesizeNowVo {

    @ApiModelProperty("目标藏品名称")
    private String collectionName;

    @ApiModelProperty("目标藏品主图")
    private List<MediaVo> mediaVos;

    @ApiModelProperty("目标藏品缩略图")
    private List<MediaVo> thumbnailImgMediaVos;

    @ApiModelProperty("目标藏品3D图")
    private List<MediaVo> threeDimensionalMediaVos;

}
