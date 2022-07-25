package com.manyun.business.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 活动合成记录表信息
 * </p>
 *
 * @author
 * @since 2022-06-29
 */
@TableName("cnt_action_record")
@ApiModel(value = "ActionRecord对象", description = "活动合成记录表信息")
@Data
public class ActionRecord implements Serializable {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("活动编号")
    private String actionId;

    @ApiModelProperty("用户编号")
    private String userId;

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("藏品编号")
    private String collectionId;

    @ApiModelProperty("藏品名称")
    private String collectionName;

    @ApiModelProperty("创建人")
    private String createdBy;

    @ApiModelProperty("创建时间")
    private LocalDateTime createdTime;

    @ApiModelProperty("更新人")
    private String updatedBy;

    @ApiModelProperty("更新时间")
    private LocalDateTime updatedTime;

}
