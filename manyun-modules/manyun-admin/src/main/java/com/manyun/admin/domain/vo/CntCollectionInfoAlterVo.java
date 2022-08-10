package com.manyun.admin.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel("藏品绑定详情接收参数视图")
public class CntCollectionInfoAlterVo {

    @ApiModelProperty("藏品故事")
    private String lookInfo;

}
