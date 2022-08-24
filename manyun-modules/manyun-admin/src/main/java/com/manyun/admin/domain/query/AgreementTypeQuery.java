package com.manyun.admin.domain.query;

import com.manyun.common.core.web.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("协议类型条件查询对象")
@Data
public class AgreementTypeQuery extends PageQuery {

    @ApiModelProperty("协议标签")
    private String agreementName;

}
