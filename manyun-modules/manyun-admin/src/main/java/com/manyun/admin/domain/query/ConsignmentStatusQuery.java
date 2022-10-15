package com.manyun.admin.domain.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("更改寄售状态请求参数")
public class ConsignmentStatusQuery {

    @NotBlank(message = "寄售id不可为空")
    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("寄售状态")
    @NotNull(message = "寄售状态不可为空")
    @Range(min = 1L,max = 2L,message = "寄售状态有误")
    private Integer status;

}
