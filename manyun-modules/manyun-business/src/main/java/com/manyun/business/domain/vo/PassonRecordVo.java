package com.manyun.business.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel("转赠记录返回视图")
public class PassonRecordVo {

    @ApiModelProperty("转出方手机号")
    private String fromPhoneNo;

    @ApiModelProperty("转入方手机号")
    private String toPhoneNo;

    @ApiModelProperty("图片链接")
    private String mediaUrl;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("转赠时间")
    private LocalDateTime createTime;


}
