package com.manyun.admin.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("寄售详情请求参数")
public class ConsignmentInfoDto {

    @ApiModelProperty(value = "寄售单号",required = true)
    @NotBlank(message= "寄售单号不能为空!")
    private String id;

    @ApiModelProperty(value = "0藏品，1盲盒",required = true)
    @NotNull(message = "参数不能为空")
    @Range(min = 0,max = 1,message = "参数传入有误")
    private Integer isType;

}
