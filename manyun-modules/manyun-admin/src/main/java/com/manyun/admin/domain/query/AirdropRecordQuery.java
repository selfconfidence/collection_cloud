package com.manyun.admin.domain.query;

import com.manyun.common.core.web.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("空投记录条件查询")
@Data
public class AirdropRecordQuery extends PageQuery
{

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("用户手机号")
    private String userPhone;

    @ApiModelProperty("藏品名称")
    private String collectionName;

}
