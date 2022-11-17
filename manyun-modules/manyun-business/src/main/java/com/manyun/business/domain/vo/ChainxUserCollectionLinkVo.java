package com.manyun.business.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/*
 * 链上查询信息
 *
 * @author yanwei
 * @create 2022-11-10
 */
@Data
@ApiModel("用户藏品链上查询交易返回视图")
public class ChainxUserCollectionLinkVo implements Serializable {

    @ApiModelProperty("藏品名称")
    public String collectionName;

    @ApiModelProperty("藏品HASH")
    private String collectionHash;

    @ApiModelProperty("拥有时间 yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

}
