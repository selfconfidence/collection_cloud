package com.manyun.business.domain.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("产品建议提交表单")
public class OpinionForm {

    @ApiModelProperty(value = "建议内容")
    @NotBlank(message = "建议内容不能为空")
    private String opinionContent;

    @ApiModelProperty(value = "图片")
    @NotBlank(message = "图片不能为空")
    private String img;

}
