package com.manyun.common.core.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/*
 *
 *
 * @author yanwei
 * @create 2022-10-22
 */
@Data
public class RequestTimeRate implements Serializable {

    @ApiModelProperty("")
    private long requestTime;
}
