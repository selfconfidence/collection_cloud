package com.manyun.common.core.web.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import java.io.Serializable;

@ApiModel("分页组件")
@Data
public class PageQuery implements Serializable {

    @ApiModelProperty(value = "起始页码",required = true)
    private  Integer pageNum;

    @ApiModelProperty(value = "查询每页多少条",required = true)
    @Max(value = 10L,message = "页码有误！")
    private  Integer pageSize;
}
