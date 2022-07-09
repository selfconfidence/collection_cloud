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
 * 用户消息
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@Data
@ToString
@Builder
public class MsgCommDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("消息标题")
    private String msgTitle;

    @ApiModelProperty("消息短语")
    private String msgForm;




}
