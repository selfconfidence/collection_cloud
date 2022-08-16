package com.manyun.admin.domain.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@ApiModel("提前购配置的商品条件查询对象")
@Data
public class PostConfigDictQuery {

    @ApiModelProperty(value = "商品类型;(0=藏品,1=盲盒)",required = true)
    @NotNull(message = "参数不能为空")
    @Range(min = 0,max = 1,message = "参数传入有误")
    private Integer isType;

}
