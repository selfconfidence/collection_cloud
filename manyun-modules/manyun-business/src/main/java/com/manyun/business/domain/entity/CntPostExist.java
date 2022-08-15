package com.manyun.business.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 提前购配置已经拥有
 * </p>
 *
 * @author yanwei
 * @since 2022-06-27
 */
@TableName("cnt_post_exist")
@ApiModel(value = "CntPostExist对象", description = "提前购配置已经拥有")
@Data
public class CntPostExist implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("藏品编号")
    private String collectionId;

    @ApiModelProperty("配置编号")
    private String configId;

    @ApiModelProperty("业务名称")
    private String buiName;

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
