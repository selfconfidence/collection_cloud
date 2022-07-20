package com.manyun.admin.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel("我的藏品视图对象")
public class UserCollectionVo implements Serializable {

    @ApiModelProperty("用户和藏品唯一标识联系")
    private String id;

    @ApiModelProperty("藏品id")
    private String collectionId;

    @ApiModelProperty("藏品名称")
    private String collectionName;

    @ApiModelProperty("藏品编号;上链后")
    private String collectionNumber;

    @ApiModelProperty("藏品哈希;上链后")
    private String collectionHash;

    @ApiModelProperty("链上地址;上链后")
    private String linkAddr;

    @ApiModelProperty("是否上链;1=未上链,2=已上链")
    private Integer isLink;

    @ApiModelProperty("来源")
    private String sourceInfo;

    @ApiModelProperty("认证机构;上链后")
    private String realCompany;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("拥有时间")
    private LocalDateTime createdTime;

    @ApiModelProperty("藏品主图")
    private List<MediaVo> mediaVos;

}
