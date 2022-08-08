package com.manyun.admin.domain.query;

import com.manyun.common.core.web.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("邀请好友送盲盒请求参数")
@Data
public class PleaseBoxQuery extends  PageQuery {

    @ApiModelProperty("盲盒名称")
    private String boxTitle;

    @ApiModelProperty("是否使用;1=使用，2=未使用")
    private Long isUse;

}
