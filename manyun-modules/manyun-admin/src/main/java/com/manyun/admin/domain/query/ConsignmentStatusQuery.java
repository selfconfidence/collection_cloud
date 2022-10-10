package com.manyun.admin.domain.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("更改寄售状态请求参数")
public class ConsignmentStatusQuery {

    @NotBlank(message = "寄售id不可为空")
    @ApiModelProperty("主键")
    private String id;

}
