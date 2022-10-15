package com.manyun.business.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 防重
 * </p>
 *
 * @author yanwei
 * @since 2022-10-11
 */
@TableName("cnt_reset")
@ApiModel(value = "cnt_reset对象", description = "防重")
@Data
@ToString
public class ReSet implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("防重复主键")
    private String resetId;


}
