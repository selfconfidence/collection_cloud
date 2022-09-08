package com.manyun.admin.domain.query;

import com.manyun.common.core.web.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("抽签规则(盲盒,藏品)返回视图")
@Data
public class CntTarQuery extends PageQuery
{

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("抽签类型;(1=盲盒,2=藏品)")
    private Integer tarType;

    @ApiModelProperty("1=正常,2=暂停(已经开奖)")
    private Integer endFlag;

}
