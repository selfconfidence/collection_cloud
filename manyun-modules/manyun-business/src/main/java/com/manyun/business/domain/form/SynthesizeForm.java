package com.manyun.business.domain.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@ApiModel("立即合成提交表单")
@Data
public class SynthesizeForm {

    @ApiModelProperty("活动id")
    @NotBlank
    private String id;
}
