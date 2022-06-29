package com.manyun.business.domain.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 藏品盲盒流转信息
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@Data
@ToString
@Builder
public class StepDto implements Serializable {



    @ApiModelProperty("模块类型")
    private String modelType;

    @ApiModelProperty("用户编号")
    private String userId;

    @ApiModelProperty("业务编号")
    private String buiId;

    @ApiModelProperty("备用字段")
    private String reMark;

    @ApiModelProperty("流转词条")
    private String formInfo;

}
