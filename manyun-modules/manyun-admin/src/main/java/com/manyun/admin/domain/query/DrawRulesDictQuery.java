package com.manyun.admin.domain.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@ApiModel("抽签规则字典条件查询对象")
@Data
public class DrawRulesDictQuery {

    @ApiModelProperty(value = "抽签类型;(1=盲盒,2=藏品)")
    private Integer tarType;

}
