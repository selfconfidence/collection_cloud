package com.manyun.comm.api.domain.redis;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 创作者表
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@ApiModel(value = "创作者视图", description = "创作者表")
@Data
@ToString
public class CreationdRedisVo implements Serializable {

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

}
