package com.manyun.admin.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/*
 *
 *
 * @author yanwei
 * @create 2022-09-25
 */
@Data
public class UserChildTermsVo implements Serializable {

    @ApiModelProperty("邀请总人数")
    private Integer terms;

    @ApiModelProperty("实名人数")
    private Integer realUsers;

    @ApiModelProperty("购买某商品得人数")
    private Integer goSellUsers;


}
