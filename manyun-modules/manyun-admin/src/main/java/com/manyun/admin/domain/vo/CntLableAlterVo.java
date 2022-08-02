package com.manyun.admin.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("绑定标签接收参数视图")
public class CntLableAlterVo
{

    @ApiModelProperty("标签编号")
    private String[] lableIds;

}
