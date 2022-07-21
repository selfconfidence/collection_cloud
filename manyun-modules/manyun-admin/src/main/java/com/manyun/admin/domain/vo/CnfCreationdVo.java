package com.manyun.admin.domain.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel("创作者返回视图")
public class CnfCreationdVo implements Serializable
{

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("创作者名称")
    private String creationName;

    @ApiModelProperty("创作者头像")
    private String headImage;

    @ApiModelProperty("创作者简介")
    private String creationInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreationName() {
        return creationName;
    }

    public void setCreationName(String creationName) {
        this.creationName = creationName;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getCreationInfo() {
        return creationInfo;
    }

    public void setCreationInfo(String creationInfo) {
        this.creationInfo = creationInfo;
    }
}
