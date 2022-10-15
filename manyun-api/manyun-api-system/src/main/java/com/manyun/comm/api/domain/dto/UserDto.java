package com.manyun.comm.api.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel("用户返回视图")
public class UserDto implements Serializable {

    @ApiModelProperty("用户编号")
    private List<String> userIds;


}
