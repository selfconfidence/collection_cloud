package com.manyun.admin.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;

@ApiModel("轮播跳转链接返回视图")
@Data
public class BannerJumpLinkVo
{

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("链接键值")
    private String linkCode;

    @ApiModelProperty("链接名称")
    private String linkName;

    @ApiModelProperty("状态 0:启用 1:停用")
    private Integer linkStatus;

    @ApiModelProperty("排序")
    private Integer linkSort;

    @ApiModelProperty("创建时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    @ApiModelProperty("更新时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;

}
