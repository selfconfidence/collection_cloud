package com.manyun.business.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 公告表
 * </p>
 *
 * @author qxh
 * @since 2022-06-23
 */
@TableName("cnt_announcement")
@ApiModel(value = "Announcement对象", description = "公告表")
@Data
public class Announcement implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("公告类型;1平台公告，2发售信息，3上新公告")
    private Integer noticeType;

    @ApiModelProperty("公告内容")
    private String noticeContent;

    @ApiModelProperty("创建人")
    private String createdBy;

    @ApiModelProperty("创建时间")
    private LocalDateTime createdTime;

    @ApiModelProperty("更新人")
    private String updatedBy;

    @ApiModelProperty("更新时间")
    private LocalDateTime updatedTime;

}
