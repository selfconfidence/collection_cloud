package com.manyun.business.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 创作者表
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@TableName("cnf_creationd")
@ApiModel(value = "CnfCreationd对象", description = "创作者表")
@Data
public class CntCreationd implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("创作者头像")
    private String headImage;

    @ApiModelProperty("创作者名称")
    private String creationName;

    @ApiModelProperty("创作者简介")
    private String creationInfo;


    @ApiModelProperty("创作者链地址")
    private String linkAddr;


    @ApiModelProperty("创建人")
    private String createdBy;

    @ApiModelProperty("创建时间")
    private LocalDateTime createdTime;

    @ApiModelProperty("更新人")
    private String updatedBy;

    @ApiModelProperty("更新时间")
    private LocalDateTime updatedTime;


}
