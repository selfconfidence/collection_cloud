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
 * 藏品系列_分类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@TableName("cnt_cate")
@ApiModel(value = "Cate对象", description = "藏品系列_分类")
@Data
@ToString
public class Cate implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("夫级别分类编号_顶层默认为0")
    private String parentId;

    @ApiModelProperty("系列_分类 1=藏品系列，2=盲盒分类")
    private Integer cateType;

    @ApiModelProperty("系列名称")
    private String cateName;

    @ApiModelProperty("系列主图")
    private String cateImage;

    @ApiModelProperty("系列详情")
    private String cateInfo;

    @ApiModelProperty("创作者编号;当前系列的创作者编号_可以为空")
    private String bindCreation;

    @ApiModelProperty("创建人")
    private String createdBy;

    @ApiModelProperty("创建时间")
    private LocalDateTime createdTime;

    @ApiModelProperty("更新人")
    private String updatedBy;

    @ApiModelProperty("更新时间")
    private LocalDateTime updatedTime;

}
