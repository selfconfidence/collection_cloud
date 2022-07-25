package com.manyun.admin.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel("活动合成附属返回视图")
public class CntActionTarVo {

    @ApiModelProperty("藏品编号")
    private String collectionId;

    @ApiModelProperty("合成所需数量")
    private Long releaseNum;

    @ApiModelProperty("合成消耗数量")
    private Long deleteNum;

}
