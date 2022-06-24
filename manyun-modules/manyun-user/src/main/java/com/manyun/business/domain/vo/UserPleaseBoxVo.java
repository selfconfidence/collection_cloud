package com.manyun.business.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("邀请盲盒奖励规则返回视图")
public class UserPleaseBoxVo implements Serializable {

    @ApiModelProperty("领取编号,点击领取的时候需要传递使用")
    private String id;

    @ApiModelProperty("邀请人数")
    private Integer pleaseNumber;

    @ApiModelProperty("限量")
    private Integer blane;

    @ApiModelProperty("奖励名称")
    private String boxName;

    @ApiModelProperty("状态  后台已经算好 根据状态展示不同流程 (1=待领取,2=已领取，3未满足条件)")
    private Integer isProcess;
    




}
