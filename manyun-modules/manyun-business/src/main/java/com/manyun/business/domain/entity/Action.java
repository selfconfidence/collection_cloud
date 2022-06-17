package com.manyun.business.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 活动表
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@TableName("cnt_action")
@ApiModel(value = "Action对象", description = "活动表")
@Data
public class Action implements Serializable {

    private static final long serialVersionUID = 1L;

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

    @ApiModelProperty("此活动合成的藏品编号")
    private String collectionId;

    @ApiModelProperty("创建人")
    private String createdBy;

    @ApiModelProperty("创建时间")
    private LocalDateTime createdTime;

    @ApiModelProperty("更新人")
    private String updatedBy;

    @ApiModelProperty("更新时间")
    private LocalDateTime updatedTime;

}
