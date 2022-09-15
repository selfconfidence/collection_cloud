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

    @ApiModelProperty("用户编号")
    private String userId;

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("用户手机号")
    private String phone;

    @ApiModelProperty("商品编号")
    private String buiId;

    @ApiModelProperty("商品名称")
    private String buiName;

    @ApiModelProperty("商品类型 1:盲盒 2:藏品")
    private String typeName;

    @ApiModelProperty("提前购次数")
    private Integer buyFrequency;

    @ApiModelProperty("备注")
    private String reMark;

    @ApiModelProperty("创建时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    @ApiModelProperty("更新时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;

}
