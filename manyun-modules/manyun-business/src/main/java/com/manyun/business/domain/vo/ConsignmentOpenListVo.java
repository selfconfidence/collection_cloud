package com.manyun.business.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.manyun.comm.api.domain.dto.CntUserDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel("寄售藏品实时大盘列表返回视图")
@ToString
public class ConsignmentOpenListVo implements Serializable {

    @ApiModelProperty("藏品名称")
    private String name;

    @ApiModelProperty("藏品图片")
    private String image;

    @ApiModelProperty("寄售价格")
    private BigDecimal price;




}
