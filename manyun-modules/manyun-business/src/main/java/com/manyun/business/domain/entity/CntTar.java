package com.manyun.business.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * <p>
 * 抽签规则(盲盒,藏品)
 * </p>
 *
 * @author yanwei
 * @since 2022-06-27
 */
@TableName("cnt_tar")
@ApiModel(value = "CntTar对象", description = "抽签规则(盲盒,藏品)")
@Data
@ToString
public class CntTar implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("抽签比例(%百分比, 抽中比例)")
    private BigDecimal tarPro;

    @ApiModelProperty("抽签类型;(1=盲盒,2=藏品)")
    private Integer tarType;

    @ApiModelProperty("创建人")
    private String createdBy;

    @ApiModelProperty("创建时间")
    private LocalDateTime createdTime;

    @ApiModelProperty("更新人")
    private String updatedBy;

    @ApiModelProperty("更新时间")
    private LocalDateTime updatedTime;

}
