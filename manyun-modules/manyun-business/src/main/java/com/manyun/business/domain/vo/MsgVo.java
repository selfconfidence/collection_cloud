package com.manyun.business.domain.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "消息返回视图", description = "用户消息")
@Data
@ToString
public class MsgVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("用户编号")
    private String userId;

    @ApiModelProperty("消息标题")
    private String msgTitle;

    @ApiModelProperty("消息短语")
    private String msgForm;

    @ApiModelProperty("消息类型  1=可对外，2=不可对外")
    private Integer msgType;



    @ApiModelProperty("创建时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;



}
