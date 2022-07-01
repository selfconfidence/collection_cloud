package com.manyun.business.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel("合成记录列表")
public class SyntheticRecordVo {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("藏品名称")
    private String collectionName;

    @ApiModelProperty("藏品主图")
    private List<MediaVo> mediaVos;

    @ApiModelProperty("创建时间")
    private LocalDateTime createdTime;

}
