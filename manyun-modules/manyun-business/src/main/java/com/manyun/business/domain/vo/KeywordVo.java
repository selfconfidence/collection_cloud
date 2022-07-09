package com.manyun.business.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("词条信息")
public class KeywordVo implements Serializable {


    @ApiModelProperty("藏品|盲盒 标题")
    private String commTitle;

    @ApiModelProperty("搜索类型 (0:藏品)(1:盲盒)")
    private Integer type;


}
