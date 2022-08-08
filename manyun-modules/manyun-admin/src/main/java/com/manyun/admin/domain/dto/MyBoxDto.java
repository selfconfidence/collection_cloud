package com.manyun.admin.domain.dto;

import com.manyun.common.core.web.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@ApiModel("我的盲盒")
@Data
public class MyBoxDto extends PageQuery {

    @ApiModelProperty(value = "用户id",required = true)
    @NotBlank(message= "用户id不能为空!")
    private String userId;

}
