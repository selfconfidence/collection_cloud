package com.manyun.admin.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("提前购配置已经拥有对象")
@Data
public class CntPostExistVo
{

    @ApiModelProperty("藏品编号")
    private String collectionId;

}
