package com.manyun.admin.domain.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("平台规则条件查询对象")
@Data
public class SystemQuery {

    @ApiModelProperty("规则名称")
    private String systemLable;

}
