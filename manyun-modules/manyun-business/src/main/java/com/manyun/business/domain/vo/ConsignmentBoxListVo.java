package com.manyun.business.domain.vo;

import com.manyun.comm.api.domain.dto.CntUserDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel("寄售盲盒列表返回视图")
@ToString
public class ConsignmentBoxListVo implements Serializable {

    @ApiModelProperty("寄售编号")
    private String id;

    @ApiModelProperty("盲盒相关视图-按需拿取即可")
    private BoxListVo boxListVo;


    @ApiModelProperty("寄售用户相关-按需拿取即可")
    private CntUserDto cntUserDto;


    @ApiModelProperty("寄售状态相关 1=已寄售(正常),2=已锁单(显示倒计时)")
    private Integer consignmentStatus;

    @ApiModelProperty("剩余支付时间 consignmentStatus = 2 有效  过了这个时间 才可以释放订单,其他人才可以抢购")
    private LocalDateTime endPayTime;


    @ApiModelProperty("寄售时间")
    private LocalDateTime createdTime;





}
