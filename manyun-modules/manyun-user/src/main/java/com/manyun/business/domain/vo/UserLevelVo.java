package com.manyun.business.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel("用户等级相关Api")
public class UserLevelVo implements Serializable {

    @ApiModelProperty("用户下级总量")
    private Integer levelCount = Integer.valueOf(0);

    @ApiModelProperty("用户下级列表 手机号已脱敏")
    private List<UserInfoVo> userInfoVos;

}
