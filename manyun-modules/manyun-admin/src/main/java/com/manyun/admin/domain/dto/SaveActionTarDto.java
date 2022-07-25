package com.manyun.admin.domain.dto;

import com.manyun.admin.domain.vo.CntActionTarVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("活动合成材料请求参数")
public class SaveActionTarDto {

    @ApiModelProperty("活动编号")
    private String actionId;

    @ApiModelProperty("活动材料相关数据")
    private List<CntActionTarVo> cntActionTarVos;

}
