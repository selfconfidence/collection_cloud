package com.manyun.common.redis.domian.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/*
 * 盲盒藏品库存已售
 *
 * @author yanwei
 * @create 2022-10-07
 */
@Data
public class BuiCronDto implements Serializable {

    @ApiModelProperty("已售数量")
    private Integer selfBalance;

    @ApiModelProperty("库存数量")
    private Integer balance;

}
