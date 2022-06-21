package com.manyun.business.domain.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 轮播表
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@ApiModel(value = "轮播返回视图", description = "轮播视图")
@Data
@ToString
public class BannerVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("轮播标题")
    private String bannerTitle;

    @ApiModelProperty("轮播图片地址")
    private String bannerImage;

    @ApiModelProperty("轮播简介 富文本")
    private String bannerInfo;

    @ApiModelProperty("轮播类型;1=首页轮播,2=竞拍轮播; 2不需要有逻辑判定,看详情即可")
    private Integer bannerType;

    @ApiModelProperty("跳转链接;bannerType=1 有效, QUICK_VIEW 跳转拍卖中心,BANNER_VIEW 查询 轮播简介,BOX_VIEW 跳转邀请好友得盲盒")
    private String jumpLink;

    @ApiModelProperty("创建时间 yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;


}
