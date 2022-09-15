package com.manyun.business.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@ApiModel("资金流水详细数据返回视图")
@Data
public class AcctSerIalListVo {

    @ApiModelProperty("交易单号")
    private String accpTxnno;

    @ApiModelProperty("出入账金额。单位 元。")
    private String amt;

    @ApiModelProperty("交易后余额。单位 元")
    private String amtBal;

    @ApiModelProperty("交易类型")
    private String txnType;

    @ApiModelProperty("商户系统交易时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date txnTime;

    @ApiModelProperty("资金流水备注")
    private String memo;

}
