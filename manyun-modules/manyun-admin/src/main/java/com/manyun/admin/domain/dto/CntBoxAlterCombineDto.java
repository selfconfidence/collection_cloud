package com.manyun.admin.domain.dto;

import com.manyun.admin.domain.vo.CntBoxAlterVo;
import com.manyun.admin.domain.vo.CntLableAlterVo;
import com.manyun.admin.domain.vo.MediaAlterVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel("盲盒变更参数组合视图")
public class CntBoxAlterCombineDto {

    @ApiModelProperty("盲盒")
    private CntBoxAlterVo cntBoxAlterVo;

    @ApiModelProperty("标签")
    private CntLableAlterVo cntLableAlterVo;

    @ApiModelProperty("图片")
    private MediaAlterVo mediaAlterVo;

}
