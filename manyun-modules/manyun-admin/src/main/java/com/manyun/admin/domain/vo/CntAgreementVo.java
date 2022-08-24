package com.manyun.admin.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel("协议相关对象返回视图")
@Data
public class CntAgreementVo
{

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("协议类型名称")
    private String agreementTypeName;

    @ApiModelProperty("协议标题")
    private String agreementTitle;

    @ApiModelProperty("协议内容")
    private String agreementInfo;

    @ApiModelProperty("创建时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    @ApiModelProperty("更新时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;

}
