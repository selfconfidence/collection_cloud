package com.manyun.admin.domain.query;

import com.manyun.common.core.web.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@ApiModel("每个用户藏品总量条件查询")
@Data
public class UserCollectionNumberQuery extends PageQuery {

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("手机号")
    private String phone;

}
