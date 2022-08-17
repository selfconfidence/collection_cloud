package com.manyun.business.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/*
 * 盲盒日志视图
 *
 * @author yanwei
 * @create 2022-08-17
 */
@Data
@ApiModel("盲盒记录返回视图")
public class BoxLogPageVo implements Serializable {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("业务编号")
    private String buiId;

    @ApiModelProperty("记录类型;0购入,1其他")
    private Integer isType;

    @ApiModelProperty("记录值-数量")
    private String formInfo;

    @ApiModelProperty("记录短语")
    private String jsonTxt;

    @ApiModelProperty("创建时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;



}
