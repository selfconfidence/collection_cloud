package com.manyun.business.domain.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "查询资金流水请求参数")
public class LLAcctserialQuery {

    @ApiModelProperty("账期开始时间")
    private Date startDate;

    @ApiModelProperty("账期结束时间")
    private Date endDate;

    @ApiModelProperty("请求页码")
    private String pageNo;

    @ApiModelProperty("每页记录数")
    private String pageSize;

}
