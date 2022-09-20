package com.manyun.admin.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("绑定图片接收参数视图")
public class MediaAlterVo implements Serializable {

    @ApiModelProperty("主图")
    private String img;

    @ApiModelProperty("三维图")
    private String threeDimensional;

}
