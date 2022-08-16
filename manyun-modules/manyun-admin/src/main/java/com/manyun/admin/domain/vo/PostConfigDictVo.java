package com.manyun.admin.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("提前购配置字典")
@Data
public class PostConfigDictVo {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("配置名称")
    private String configName;

}
