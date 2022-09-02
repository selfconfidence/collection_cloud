package com.manyun.business.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 盲盒与藏品中间表
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@TableName("cnt_box_collection")
@ApiModel(value = "BoxCollection对象", description = "盲盒与藏品中间表")
@Data
public class BoxCollection implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("盲盒编号")
    private String boxId;

    @ApiModelProperty("藏品编号")
    private String collectionId;

    @ApiModelProperty("藏品名称;反三范式")
    private String collectionName;

    @ApiModelProperty("藏品原价;反三范式")
    private BigDecimal sourcePrice;

    @ApiModelProperty("概率 百分比;box_id 总和不能超过 100%")
    private BigDecimal tranSvg;

    @ApiModelProperty("评分等级;SSR SR 等等")
    private String flagScore;

    @ApiModelProperty("创建人")
    private String createdBy;

    @ApiModelProperty("库存数量")
    private Integer openQuantity;

    @ApiModelProperty("已开数量")
    private Integer openNumber;

    @ApiModelProperty("创建时间")
    private LocalDateTime createdTime;

    @ApiModelProperty("更新人")
    private String updatedBy;

    @ApiModelProperty("更新时间")
    private LocalDateTime updatedTime;

}
