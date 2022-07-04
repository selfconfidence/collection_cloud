package com.manyun.business.domain.query;

import com.manyun.common.core.web.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@ApiModel("寄售订单分页查询项")
public class ConsignmentOrderQuery extends PageQuery {

    @ApiModelProperty("状态 1=寄售中,2=已锁单(交易中)  3=已完成 | null 为查所有")
    private Integer consignmentStatus;

}
