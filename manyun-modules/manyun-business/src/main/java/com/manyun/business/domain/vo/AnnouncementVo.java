package com.manyun.business.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel("公告通知返回视图")
public class AnnouncementVo implements Serializable {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("公告类型;1平台公告，2发售信息，3上新公告")
    private Integer noticeType;

    @ApiModelProperty("公告标题")
    private String noticeTitle;

    @ApiModelProperty("公告内容")
    private String noticeContent;

    @ApiModelProperty("创建时间 yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

}
