package com.manyun.business.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserCollectionCountDto {

    @ApiModelProperty("藏品id")
    private String collectionId;

    @ApiModelProperty("拥有数量")
    private Integer count;

}
