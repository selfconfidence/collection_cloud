package com.manyun.business.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/*
 * 阿里实名认证返回vo
 *
 * @author yanwei
 * @create 2022-08-24
 */
@Data
@ApiModel("阿里h5金融版实名认证返回vo")
public class AliRealVo implements Serializable {

    @ApiModelProperty("认证Id用来回调服务端认证接口")
    private String certifyId;

    @ApiModelProperty("认证Url用来跳转认证")
    private String certifyUrl;

}
