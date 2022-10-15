package com.manyun.admin.domain.vo;

import com.manyun.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("规定时间统计商品查询返回视图")
public class StatisticalGoodsVo {

    @ApiModelProperty("用户Id")
    @Excel(name = "用户id",width = 25)
    private String userId;

    @ApiModelProperty("用户电话号")
    @Excel(name = "用户手机号",width = 20)
    private String phone;

    @ApiModelProperty("区块链地址")
    @Excel(name = "区块链地址",width = 80)
    private String linkAddr;

    @ApiModelProperty("藏品")
    private List<GoodsVo> collections;

    @ApiModelProperty("盲盒")
    private List<GoodsVo> boxs;

}
