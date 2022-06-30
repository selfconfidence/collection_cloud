package com.manyun.business.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 反馈举报;反馈举报主体表
 * </p>
 *
 * @author
 * @since 2022-06-28
 */
@TableName("cnt_feedback")
@ApiModel(value = "反馈举报对象", description = "反馈举报;反馈举报主体表")
@Data
public class Feedback {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("反馈用户id")
    private String feedbackUserId;

    @ApiModelProperty("反馈用户名称")
    private String feedbackUserName;

    @ApiModelProperty("反馈用户手机号")
    private String feedbackUserPhone;

    @ApiModelProperty("区块链地址")
    private String linkAddr;

    @ApiModelProperty("反馈类型")
    private String feedbackType;

    @ApiModelProperty("反馈用户内容")
    private String feedbackContent;

    @ApiModelProperty("反馈图片")
    private String feedbackImg;

    @ApiModelProperty("创建人")
    private String createdBy;

    @ApiModelProperty("创建时间")
    private LocalDateTime createdTime;

    @ApiModelProperty("修改人")
    private String updatedBy;

    @ApiModelProperty("修改时间")
    private LocalDateTime updatedTime;

}
