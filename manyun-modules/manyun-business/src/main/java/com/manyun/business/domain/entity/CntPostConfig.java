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
 * 提前购配置表
 * </p>
 *
 * @author yanwei
 * @since 2022-06-27
 */
@TableName("cnt_post_config")
@ApiModel(value = "TbPostConfig对象", description = "提前购配置表")
@Data
@ToString
public class CntPostConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("配置名称")
    private String configName;

    @ApiModelProperty("购买次数")
    private Integer buyFrequency;


    @ApiModelProperty("业务编号(藏品|盲盒)")
    private String buiId;

    @ApiModelProperty("0=藏品,1=盲盒")
    private Integer isType;

    @ApiModelProperty("备注")
    private String reMark;

    @ApiModelProperty("创建人")
    private String createdBy;

    @ApiModelProperty("创建时间")
    private LocalDateTime createdTime;

    @ApiModelProperty("更新人")
    private String updatedBy;

    @ApiModelProperty("更新时间")
    private LocalDateTime updatedTime;

}
