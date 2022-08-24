package com.manyun.admin.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@ApiModel("协议类型对象返回视图")
@Data
public class CntAgreementTypeVo implements Serializable
{

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("协议键值")
    private Integer agreementCode;

    @ApiModelProperty("协议标签")
    private String agreementName;

    @ApiModelProperty("状态 0:启用 1:停用")
    private Long agreementStatus;

    @ApiModelProperty("排序")
    private Long agreementSort;

    @ApiModelProperty("创建时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    @ApiModelProperty("更新时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;

}
