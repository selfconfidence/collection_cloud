package com.manyun.admin.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;

@ApiModel("客服对象返回视图")
@Data
public class CntCustomerServiceVo
{

    @ApiModelProperty("菜单ID")
    private Integer id;

    @ApiModelProperty("菜单名称")
    private String menuName;

    @ApiModelProperty("父菜单ID")
    private Integer parentId;

    @ApiModelProperty("父菜单名称")
    private String parentName;

    @ApiModelProperty("显示顺序")
    private Integer orderNum;

    @ApiModelProperty("菜单状态（0正常 1停用）")
    private String menuStatus;

    @ApiModelProperty("文章标题")
    private String articleTitle;

    @ApiModelProperty("文章详情")
    private String articleContent;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private Date createTime;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("更新时间")
    private Date updateTime;

}
