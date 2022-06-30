package com.manyun.business.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel("合成记录列表")
public class SyntheticRecordVo {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("藏品名称")
    private String collectionName;

    @ApiModelProperty("藏品图片")
    private String collectionImage;

    @ApiModelProperty("创建时间")
    private LocalDateTime createdTime;

}
