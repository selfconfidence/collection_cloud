package com.manyun.business.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("客服中心问题文章")
public class ArticleVo {

    @ApiModelProperty("文章标题")
    private String articleTitle;

    @ApiModelProperty("文章详情")
    private String articleContent;

}
