package com.manyun.admin.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;

@ApiModel("产品建议返回视图")
@Data
public class CntOpinionVo
{

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("建议用户名称")
    private String opinionUserName;

    @ApiModelProperty("建议用户手机号")
    private String opinionUserPhone;

    @ApiModelProperty("建议内容")
    private String opinionContent;

    @ApiModelProperty("图片")
    private String img;

    @ApiModelProperty("处理状态（0:未处理 1:已处理）")
    private String status;

    @ApiModelProperty("创建时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    @ApiModelProperty("更新时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;

}
