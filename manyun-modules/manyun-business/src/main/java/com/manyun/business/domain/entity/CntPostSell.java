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
 * 提前购配置可以购买
 * </p>
 *
 * @author yanwei
 * @since 2022-06-27
 */
@TableName("cnt_post_sell")
@ApiModel(value = "CntPostSell对象", description = "提前购配置可以购买")
@Data
@ToString
public class CntPostSell implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("业务编号;（盲盒 & 藏品）编号")
    private String buiId;

    @ApiModelProperty("配置编号")
    private String configId;

    @ApiModelProperty("业务名称")
    private String buiName;

    @ApiModelProperty("创建人")
    private String createdBy;

    @ApiModelProperty("创建时间")
    private LocalDateTime createdTime;

    @ApiModelProperty("更新人")
    private String updatedBy;

    @ApiModelProperty("更新时间")
    private LocalDateTime updatedTime;

}
