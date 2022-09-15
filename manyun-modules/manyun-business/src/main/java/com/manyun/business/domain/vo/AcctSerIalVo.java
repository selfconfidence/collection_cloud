package com.manyun.business.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("资金流水数据返回视图")
@Data
public class AcctSerIalVo {

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("出账总金额，表示当前查询条件下的出账总金额，单位：元。")
    private String totalOutAmt;

    @ApiModelProperty("入账总金额，表示当前查询条件下的入账总金额，单位：元。")
    private String totalInAmt;

    @ApiModelProperty("资金流水详细数据")
    private List<AcctSerIalListVo> acctSerIalListVos;

}
