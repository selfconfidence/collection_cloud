package com.manyun.business.domain.vo;

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
 * 藏品和标签中间表
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@ApiModel(value = "藏品标签视图", description = "藏品标签视图")
@ToString
@Data
public class LableVo implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("标签名称")
    private String lableName;
}
