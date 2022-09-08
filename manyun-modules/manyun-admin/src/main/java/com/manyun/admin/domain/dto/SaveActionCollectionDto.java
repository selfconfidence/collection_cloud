package com.manyun.admin.domain.dto;

import com.manyun.admin.domain.vo.ActionCollectionVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("活动合成目标藏品请求参数")
public class SaveActionCollectionDto {

    @ApiModelProperty("活动编号")
    private String actionId;

    @ApiModelProperty("活动合成目标藏品相关数据")
    private List<ActionCollectionVo> actionCollectionVos;

}
