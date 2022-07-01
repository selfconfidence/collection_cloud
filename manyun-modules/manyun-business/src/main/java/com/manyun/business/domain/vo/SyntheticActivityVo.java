package com.manyun.business.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel("合成活动列表")
public class SyntheticActivityVo {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("活动标题")
    private String actionTitle;

    @ApiModelProperty("活动封面")
    private String actionImage;

    @ApiModelProperty("活动状态;1=待开始,2=进行中,3=结束")
    private Integer actionStatus;

    @ApiModelProperty("活动开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("活动结束时间")
    private LocalDateTime endTime;

}
