package com.manyun.business.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel("客服页面的详细信息")
public class CustomerServiceVo {

    @ApiModelProperty("菜单Id")
    private Long menuId;

    @ApiModelProperty("菜单名称")
    private String menuName;

    @ApiModelProperty("父菜单Id")
    private Long parentId;

    @ApiModelProperty("子菜单")
    private List<CustomerServiceVo> children = new ArrayList<CustomerServiceVo>();

}
