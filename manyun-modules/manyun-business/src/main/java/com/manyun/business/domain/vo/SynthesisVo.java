package com.manyun.business.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class SynthesisVo {

    @ApiModelProperty("活动编号")
    private String actionId;

    @ApiModelProperty("目标藏品id")
    private String collectionId;

    @ApiModelProperty("目标藏品名称")
    private String collectionName;

    @ApiModelProperty("藏品主图")
    private List<MediaVo> mediaVos;

    @ApiModelProperty("合成材料")
    private List<MaterialVo> materials;

    @ApiModelProperty("规则内容")
    private String ruleContent;

    @ApiModelProperty("合并状态 1材料不足 2材料充足")
    private Integer mergeStatus;

}
