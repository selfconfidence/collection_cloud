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

}
