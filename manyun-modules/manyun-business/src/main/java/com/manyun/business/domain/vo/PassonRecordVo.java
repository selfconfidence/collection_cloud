package com.manyun.business.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @ApiModelProperty("转赠时间 yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;


}
