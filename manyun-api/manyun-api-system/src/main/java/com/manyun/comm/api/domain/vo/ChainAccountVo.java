package com.manyun.comm.api.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("蚂蚁链创建账户返回视图")
public class ChainAccountVo implements Serializable {

    @ApiModelProperty("用户真实区块链地址")
    private String link_user;

    @ApiModelProperty("用户私钥")
    private String user_key;



}
