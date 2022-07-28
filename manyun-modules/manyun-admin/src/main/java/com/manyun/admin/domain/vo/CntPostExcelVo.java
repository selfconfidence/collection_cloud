package com.manyun.admin.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel("提前购格返回视图")
@Data
public class CntPostExcelVo
{

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("用户编号;内部字段")
    private String userId;

    @ApiModelProperty("用户;用户手机号")
    private String phone;

    @ApiModelProperty("业务编号;(盲盒 & 藏品) 编号")
    private String buiId;

    @ApiModelProperty("业务名称;（盲盒 & 藏品）名称")
    private String buiName;

    @ApiModelProperty("标识;（盲盒 & 藏品）  词条")
    private String typeName;

    @ApiModelProperty("备注")
    private String reMark;

    @ApiModelProperty("创建时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    @ApiModelProperty("更新时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;

}
