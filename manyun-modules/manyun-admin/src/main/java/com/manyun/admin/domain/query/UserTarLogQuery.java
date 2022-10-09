package com.manyun.admin.domain.query;

import com.manyun.common.core.web.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("抽签记录日志条件查询")
@Data
public class UserTarLogQuery extends PageQuery
{

    @ApiModelProperty("手机号")
    private String userPhone;

}
