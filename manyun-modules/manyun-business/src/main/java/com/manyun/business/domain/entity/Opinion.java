package com.manyun.business.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 建议;产品建议主体表
 * </p>
 *
 * @author
 * @since 2022-06-28
 */
@TableName("cnt_opinion")
@ApiModel(value = "产品建议对象", description = "建议;产品建议主体表")
@Data
public class Opinion {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("建议用户id")
    private String opinionUserId;

    @ApiModelProperty("建议用户名称")
    private String opinionUserName;

    @ApiModelProperty("建议用户手机号")
    private String opinionUserPhone;

    @ApiModelProperty("建议用户内容")
    private String opinionContent;

    @ApiModelProperty("图片")
    private String img;

    @ApiModelProperty("创建人")
    private String createdBy;

    @ApiModelProperty("创建时间")
    private LocalDateTime createdTime;

    @ApiModelProperty("修改人")
    private String updatedBy;

    @ApiModelProperty("修改时间")
    private LocalDateTime updatedTime;

}
