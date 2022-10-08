package com.manyun.admin.domain.redis.box;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 盲盒与藏品中间表
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@ApiModel(value = "盲盒关联藏品返回视图", description = "盲盒与藏品视图")
@Data
public class BoxCollectionJoinVo implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty("盲盒编号")
    private String boxId;

    @ApiModelProperty("藏品编号")
    private String collectionId;

    @ApiModelProperty("藏品名称;")
    private String collectionName;

    @ApiModelProperty("藏品原价;")
    private BigDecimal sourcePrice;

    @ApiModelProperty("概率 百分比;")
    private BigDecimal tranSvg;

    @ApiModelProperty("评分等级;SSR SR 等等")
    private String flagScore;

}
