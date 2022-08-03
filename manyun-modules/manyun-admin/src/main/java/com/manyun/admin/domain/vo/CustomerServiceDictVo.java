package com.manyun.admin.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("客服字典")
public class CustomerServiceDictVo {

    @ApiModelProperty("菜单ID")
    private Integer id;

    @ApiModelProperty("菜单名称")
    private String menuName;

}
