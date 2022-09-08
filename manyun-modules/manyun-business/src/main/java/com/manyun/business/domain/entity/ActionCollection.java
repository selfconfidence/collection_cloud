package com.manyun.business.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@ApiModel("活动合成目标藏品对象")
@Data
@TableName("cnt_action_collection")
public class ActionCollection implements Serializable
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("活动编号")
    private String actionId;

    @ApiModelProperty("此活动合成的藏品编号")
    private String collectionId;

    @ApiModelProperty("概率 百分比  总和不能超过 100%")
    private BigDecimal tranSvg;

    @ApiModelProperty("库存数量")
    private Integer actionQuantity;

    @ApiModelProperty("已开数量")
    private Integer actionNumber;

    @ApiModelProperty("创建人")
    private String createdBy;

    @ApiModelProperty("创建时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    @ApiModelProperty("更新人")
    private String updatedBy;

    @ApiModelProperty("更新时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedTime;

}
