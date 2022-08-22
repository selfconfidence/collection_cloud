package com.manyun.business.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 盲盒评分表
 * </p>
 *
 * @author 
 * @since 2022-08-22
 */
@TableName("cnt_box_score")
@ApiModel(value = "CntBoxScore对象", description = "盲盒评分表")
@Data
public class CntBoxScore implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("评分名称")
    private String scoreName;

    @ApiModelProperty("状态 0:启用 1:停用")
    private String scoreStatus;

    @ApiModelProperty("排序")
    private Integer scoreSort;

    @ApiModelProperty("创建人")
    private String createdBy;

    @ApiModelProperty("创建时间")
    private LocalDateTime createdTime;

    @ApiModelProperty("更新人")
    private String updatedBy;

    @ApiModelProperty("更新时间")
    private LocalDateTime updatedTime;

}
