package com.manyun.business.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("绑卡列表响应参数")
public class LinkedacctVo {

    @ApiModelProperty("银行预留手机号")
    private String linkedPhone;

    @ApiModelProperty("开户行名称")
    private String linkedBrbankname;

    @ApiModelProperty("银行卡号")
    private String linkedAcctno;

}
