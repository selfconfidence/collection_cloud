package com.manyun.admin.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("重试上链返回视图")
public class MyChainxVo {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("用户电话号")
    private String phone;

    @ApiModelProperty("藏品Id")
    private String collectionId;

    @ApiModelProperty("藏品名称")
    private String collectionName;

    @ApiModelProperty("藏品缩略图")
    private List<MediaVo> thumbnailImgMediaVos;

    @ApiModelProperty("来源")
    private String sourceInfo;

    @ApiModelProperty("失败信息")
    private String collectionHash;

}
