package com.manyun.admin.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("发行方返回视图")
public class CntCollectionInfoVo {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("藏品名称")
    private String collectionName;

    @ApiModelProperty("发行方")
    private String publishOther;

    @ApiModelProperty("发行方头像")
    private String publishAuther;

    @ApiModelProperty("藏品故事")
    private String lookInfo;

    @ApiModelProperty("购买须知")
    private String customerTail;

}
