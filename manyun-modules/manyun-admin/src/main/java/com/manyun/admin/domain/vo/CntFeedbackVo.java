package com.manyun.admin.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;

@ApiModel("产品举报反馈视图")
@Data
public class CntFeedbackVo
{

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("反馈用户名称")
    private String feedbackUserName;

    @ApiModelProperty("反馈用户手机号")
    private String feedbackUserPhone;

    @ApiModelProperty("区块链地址")
    private String linkAddr;

    @ApiModelProperty("举报类型 1:举报他人炒作/交易藏品   2:举报他人使用外挂等违规手段抢购藏品  3:其他")
    private String feedbackType;

    @ApiModelProperty("反馈内容")
    private String feedbackContent;

    @ApiModelProperty("反馈图片")
    private String feedbackImg;

    @ApiModelProperty("处理状态（0:未处理 1:已处理）")
    private String status;

    @ApiModelProperty("创建时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    @ApiModelProperty("更新时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;

}
