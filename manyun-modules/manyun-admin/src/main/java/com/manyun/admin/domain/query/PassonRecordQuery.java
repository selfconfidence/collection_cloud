package com.manyun.admin.domain.query;

import com.manyun.common.core.web.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@ApiModel("转赠记录条件查询")
@Data
public class PassonRecordQuery extends PageQuery
{

    @ApiModelProperty("转出用户昵称")
    private String oldNickName;

    @ApiModelProperty("转入用户昵称")
    private String newNickName;

    @ApiModelProperty("商品名称")
    private String goodsName;

}
