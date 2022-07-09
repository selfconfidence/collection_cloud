package com.manyun.business.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.sql.Blob;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * <p>
 * 通知公告表
 * </p>
 *
 * @author yanwei
 * @since 2022-07-08
 */
@TableName("cnt_notice")
@ApiModel(value = "CntNotice对象", description = "通知公告表")
@Data
@ToString
public class CntNotice implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("公告ID")
    private String id;

    @ApiModelProperty("公告标题")
    private String noticeTitle;

    @ApiModelProperty("公告类型（1通知 2公告）")
    private Integer noticeType;

    @ApiModelProperty("公告内容")
    private Blob noticeContent;

    @ApiModelProperty("公告状态（0正常 1关闭）")
    private Integer status;

    @ApiModelProperty("创建者")
    private String createBy;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新者")
    private String updateBy;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("备注")
    private String remark;

}
