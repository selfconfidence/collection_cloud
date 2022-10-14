package com.manyun.admin.domain.query;

import com.manyun.common.core.web.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("藏品编号查询条件查询对象")
public class CollectionNumberQuery extends PageQuery {

    @ApiModelProperty("藏品id")
    @NotBlank(message = "请选择藏品")
    private String collectionId;

}
