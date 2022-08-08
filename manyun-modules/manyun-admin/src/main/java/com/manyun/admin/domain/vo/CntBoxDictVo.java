package com.manyun.admin.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("盲盒字典")
public class CntBoxDictVo {

    @ApiModelProperty("盲盒id")
    private String id;

    @ApiModelProperty("盲盒名称")
    private String boxTitle;

}
