package com.manyun.business.domain.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 媒体存储器
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@Data
@ApiModel(value = "媒体视图", description = "媒体存储器")
public class MediaVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("业务编号")
    private String buiId;

    @ApiModelProperty("模块类型")
    private String modelType;

    @ApiModelProperty("存储地址")
    private String mediaUrl;

    @ApiModelProperty("媒体类型")
    private String mediaType;


}
