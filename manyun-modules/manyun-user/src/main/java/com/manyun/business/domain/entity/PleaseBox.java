package com.manyun.business.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * <p>
 * 邀请好友送盲盒规则
 * </p>
 *
 * @author yanwei
 * @since 2022-06-24
 */
@TableName("cnt_please_box")
@ApiModel(value = "PleaseBox对象", description = "邀请好友送盲盒规则")
@Data
@ToString
public class PleaseBox implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("邀请人数;邀请人数")
    private Integer pleaseNumber;

    @ApiModelProperty("奖励的盲盒编号")
    private String boxId;

    @ApiModelProperty("已售")
    private Integer selfBalance;

    @ApiModelProperty("库存")
    private Integer balance;

    @ApiModelProperty("是否使用;1=使用，2=未使用")
    private Integer isUse;

    @ApiModelProperty("创建人")
    private String createdBy;

    @ApiModelProperty("创建时间")
    private LocalDateTime createdTime;

    @ApiModelProperty("更新人")
    private String updatedBy;

    @ApiModelProperty("更新时间")
    private LocalDateTime updatedTime;

}
