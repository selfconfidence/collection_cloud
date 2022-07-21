package com.manyun.admin.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("标签管理返回视图")
public class CntLableVo
{

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("排序")
    private Integer orderNum;

    @ApiModelProperty("标签名称")
    private String lableName;

}
