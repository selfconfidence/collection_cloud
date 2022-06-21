package com.manyun.business.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class LogInfoDto implements Serializable {

    private String buiId;

    private Integer isType;

    private String formInfo;

    private String jsonTxt;

    private String modelType;


}
