package com.manyun.admin.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;



@ApiModel("发行方字典")
@Data
public class CnfIssuanceDictVo
{

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("发行方名称")
    private String publishOther;

}
