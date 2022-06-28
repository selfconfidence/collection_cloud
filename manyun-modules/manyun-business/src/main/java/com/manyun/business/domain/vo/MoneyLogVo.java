package com.manyun.business.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ToString
@ApiModel("钱包记录收支返回视图")
public class MoneyLogVo implements Serializable {


    @ApiModelProperty("1=支出 ，2=收入")
    private Integer isType;

    @ApiModelProperty("实际金额")
    private String formInfo;

    @ApiModelProperty("收支标题")
    private String jsonTxt;

    @ApiModelProperty("创建时间 yyyy-MM-dd HH:mm:ss"  )
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;


}
