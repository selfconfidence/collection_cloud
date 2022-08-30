package com.manyun.admin.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


@ApiModel("轮播图对象返回视图")
@Data
public class CntBannerVo
{

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("轮播标题")
    private String bannerTitle;

    @ApiModelProperty("轮播图片地址")
    private String bannerImage;

    @ApiModelProperty("轮播简介")
    private String bannerInfo;

    @ApiModelProperty("轮播类型;1=首页轮播,2=竞拍轮播")
    private Integer bannerType;

    @ApiModelProperty("跳转链接 QUICK_VIEW 跳转拍卖中心,BANNER_VIEW 查询轮播简介,BOX_VIEW 跳转邀请好友得盲盒")
    private String jumpLink;

    @ApiModelProperty("跳转链接名称")
    private String jumpLinkName;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private Date createdTime;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("更新时间")
    private Date updatedTime;

}
