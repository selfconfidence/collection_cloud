package com.manyun.admin.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel("藏品绑定详情接收参数视图")
public class CntCollectionInfoAlterVo {

    @ApiModelProperty("发行方")
    private String publishOther;

    @ApiModelProperty("购买须知")
    private String customerTail;

}
