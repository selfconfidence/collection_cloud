package com.manyun.admin.domain.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.manyun.common.core.web.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel("盲盒条件查询对象")
@Data
public class BoxQuery extends PageQuery {

    @ApiModelProperty("盲盒状态;0=下架,1=正常,2=售罄 ")
    private Integer statusBy;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("发售时间")
    private Date publishTime;

}
