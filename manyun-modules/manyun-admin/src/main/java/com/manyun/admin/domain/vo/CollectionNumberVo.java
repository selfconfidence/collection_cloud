package com.manyun.admin.domain.vo;

import com.manyun.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("藏品编号查询返回视图")
public class CollectionNumberVo {

    @ApiModelProperty("用户Id")
    @Excel(name = "用户id",width = 25)
    private String userId;

    @ApiModelProperty("用户电话号")
    @Excel(name = "用户手机号",width = 20)
    private String phone;

    @ApiModelProperty("区块链地址")
    @Excel(name = "区块链地址",width = 80)
    private String linkAddr;

    @ApiModelProperty("藏品名称")
    @Excel(name = "藏品名称", width = 30)
    private String collectionName;

    @ApiModelProperty("藏品编号")
    @Excel(name = "藏品编号",width = 25)
    private String collectionNumber;

}
