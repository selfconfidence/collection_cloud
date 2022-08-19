package com.manyun.admin.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@ApiModel("盲盒评分字典视图")
@Data
public class CntBoxScoreDictVo
{

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("评分名称")
    private String scoreName;

}
