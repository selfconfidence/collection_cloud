package com.manyun.business.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/*
 * 链上查询信息
 *
 * @author yanwei
 * @create 2022-11-10
 */
@Data
@ApiModel("用户链上查询交易返回视图")
public class ChainxUserLinkVo implements Serializable {

    @ApiModelProperty("用户区块链地址")
    public String linkAddr;

    @ApiModelProperty("注册时间 yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    @ApiModelProperty("拥有的藏品列表，长度就是 总数量")
    private List<ChainxUserCollectionLinkVo> userCollections;


}
