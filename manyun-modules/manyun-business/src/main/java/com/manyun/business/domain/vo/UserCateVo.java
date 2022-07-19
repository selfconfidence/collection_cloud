package com.manyun.business.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 藏品系列_分类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@ApiModel(value = "用户系列藏品相关返回视图", description = "用户系列藏品相关返回视图")
@Data
@ToString
public class UserCateVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("系列编号")
    private String id;

    @ApiModelProperty("系列名称")
    private String cateName;

    @ApiModelProperty("系列主图")
    private String cateImage;

    @ApiModelProperty("创作者头像")
    private String headImage;

    @ApiModelProperty("创作者名称")
    private String creationName;


    @ApiModelProperty("系列下藏品列表")
    private List<UserCateCollectionVo> userCateCollectionVos;







}
