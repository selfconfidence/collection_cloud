package com.manyun.admin.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel("提前购配置查询结果")
@Data
public class CntPostConfigBeanDto
{

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("配置名称")
    private String configName;

    @ApiModelProperty("备注")
    private String reMark;

    @ApiModelProperty("商品类型 0=藏品,1=盲盒")
    private Integer isType;

    @ApiModelProperty("藏品名称")
    private String collectionName;

    @ApiModelProperty("盲盒标题")
    private String boxTitle;

    @ApiModelProperty("创建时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    @ApiModelProperty("更新时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;

}
