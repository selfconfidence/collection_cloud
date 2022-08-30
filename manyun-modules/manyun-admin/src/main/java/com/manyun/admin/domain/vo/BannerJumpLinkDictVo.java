package com.manyun.admin.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("轮播跳转链接字典")
@Data
public class BannerJumpLinkDictVo {

    @ApiModelProperty("链接键值")
    private String linkCode;

    @ApiModelProperty("链接名称")
    private String linkName;

}
