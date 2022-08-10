package com.manyun.admin.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


@ApiModel("发行方返回视图")
@Data
public class CnfIssuanceVo
{

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("发行方名称")
    private String publishOther;

    @ApiModelProperty("发行方头像")
    private String publishAuther;

    @ApiModelProperty("创建时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    @ApiModelProperty("更新时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;

}
