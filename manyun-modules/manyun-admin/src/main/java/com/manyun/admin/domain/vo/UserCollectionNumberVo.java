package com.manyun.admin.domain.vo;

import com.manyun.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("每个用户藏品总量返回视图")
public class UserCollectionNumberVo {

    @ApiModelProperty("用户Id")
    @Excel(name = "用户id",width = 25)
    private String userId;

    @ApiModelProperty("用户电话号")
    @Excel(name = "用户手机号",width = 20)
    private String phone;

    @ApiModelProperty("区块链地址")
    @Excel(name = "区块链地址",width = 80)
    private String linkAddr;

    @ApiModelProperty("持有数量")
    @Excel(name = "持有数量")
    private Integer count;

}
