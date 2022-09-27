package com.manyun.business.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("新增绑卡响应参数")
public class BindCardVo {

    @ApiModelProperty("流水号")
    private String txnSeqno;

    @ApiModelProperty("授权令牌")
    private String token;

}
