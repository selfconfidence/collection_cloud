package com.manyun.business.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MaterialVo {

    @ApiModelProperty("材料id")
    private String materialId;

    @ApiModelProperty("材料名称")
    private String materialName;

    @ApiModelProperty("材料图片")
    private String materialImage;

    @ApiModelProperty("所需的数量")
    private Integer releaseNum;

    @ApiModelProperty("消耗的数量")
    private Integer deleteNum;

    @ApiModelProperty("用户拥有的数量")
    private Integer numCount;

}
