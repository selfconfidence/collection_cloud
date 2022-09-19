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

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("商品类型 0:藏品 1:空投")
    private Integer goodsType;

    @ApiModelProperty("投递类型 0:空投 1:批量空投")
    private Integer deliveryType;

    @ApiModelProperty("投递状态 0:投递成功 1:投递失败")
    private Integer deliveryStatus;

}
