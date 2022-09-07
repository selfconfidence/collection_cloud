package com.manyun.business.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class SynthesisVo {

    @ApiModelProperty("活动编号")
    private String actionId;

    @ApiModelProperty("活动状态;1=待开始,2=进行中,3=结束")
    private Integer actionStatus;

    @ApiModelProperty("活动标题")
    private String actionTitle;

    @ApiModelProperty("活动封面")
    private String actionImage;

    @ApiModelProperty("合成材料")
    private List<MaterialVo> materials;

    @ApiModelProperty("规则内容")
    private String ruleContent;

    @ApiModelProperty("合并状态 1材料不足 2材料充足")
    private Integer mergeStatus;

}
