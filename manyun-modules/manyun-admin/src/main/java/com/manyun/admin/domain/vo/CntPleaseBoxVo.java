package com.manyun.admin.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel("邀请好友送盲盒返回视图")
@Data
public class CntPleaseBoxVo {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("邀请人数;邀请人数")
    private Long pleaseNumber;

    @ApiModelProperty("盲盒名称")
    private String boxTitle;

    @ApiModelProperty("已售")
    private Long selfBalance;

    @ApiModelProperty("库存")
    private Long balance;

    @ApiModelProperty("是否使用;1=使用，2=未使用")
    private Long isUse;

    @ApiModelProperty("创建时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    @ApiModelProperty("更新时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;

}
