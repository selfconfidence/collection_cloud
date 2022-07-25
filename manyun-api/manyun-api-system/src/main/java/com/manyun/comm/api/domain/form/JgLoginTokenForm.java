package com.manyun.comm.api.domain.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("激光一键token提交表单")
@Data
public class JgLoginTokenForm {

    @ApiModelProperty("认证SDK获取到的loginToken")
    private String loginToken;


}
