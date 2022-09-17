package com.manyun.admin.domain.query;

import com.manyun.common.core.web.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@ApiModel("系统余额提现对象条件查询")
@Data
public class SystemWithdrawQuery extends PageQuery
{

    @ApiModelProperty("姓名")
    private String userName;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("银行卡")
    private String bankCard;

    @ApiModelProperty("是否已打款（0未打款，1已打款）")
    private Integer withdrawStatus;

}
