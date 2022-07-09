package com.manyun.business.domain.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Blob;
import java.time.LocalDateTime;

/**
 * <p>
 * 通知公告表
 * </p>
 *
 * @author yanwei
 * @since 2022-07-08
 */
@Data
@ApiModel(value = "系统公告返回视图", description = "通知公告表")
public class CntNoticeVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("公告ID")
    private String id;

    @ApiModelProperty("公告标题")
    private String noticeTitle;

    @ApiModelProperty("公告类型（1通知 2公告）")
    private Integer noticeType;

    @ApiModelProperty("公告内容 - 富文本")
    private String noticeContent;

    @ApiModelProperty("创建时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty("备注")
    private String remark;


}
