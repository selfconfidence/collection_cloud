package com.manyun.admin.domain.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("用户管理列表筛选视图")
public class UserMoneyQuery {

    @ApiModelProperty("真实姓名")
    private String realName;

    @ApiModelProperty("真实手机号")
    private String realPhone;

}
