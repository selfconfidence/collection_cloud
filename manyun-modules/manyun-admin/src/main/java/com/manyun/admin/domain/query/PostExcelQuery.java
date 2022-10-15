package com.manyun.admin.domain.query;

import com.manyun.common.core.web.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel("提前够表格方式条件查询对象")
public class PostExcelQuery extends PageQuery {

    @ApiModelProperty("手机号")
    private String phone;

}
