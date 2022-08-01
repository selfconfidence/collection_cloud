package com.manyun.business.domain.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.manyun.common.core.web.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@ToString
@ApiModel("钱包记录查询视图")
public class MoneyLogQuery extends PageQuery  {


    @ApiModelProperty("1=支出 ，2=收入,null 查所有")
    private Integer isType;


    @ApiModelProperty("时间查询 yyyy-MM-dd"  )
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private LocalDate createdTime;


}
