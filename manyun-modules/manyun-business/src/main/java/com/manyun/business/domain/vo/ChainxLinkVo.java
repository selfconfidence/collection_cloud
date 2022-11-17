package com.manyun.business.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/*
 * 链上查询信息
 *
 * @author yanwei
 * @create 2022-11-10
 */
@Data
@ApiModel("链上查询交易返回视图")
public class ChainxLinkVo<T> implements Serializable {

    @ApiModelProperty("视图标识 1 =用户区块链查询，2=藏品流转信息")
    public Integer linkFlag = 1;

    @ApiModelProperty("返回视图相关数据结构")
    private T data;

}
