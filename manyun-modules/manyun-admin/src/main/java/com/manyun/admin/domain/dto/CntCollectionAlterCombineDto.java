package com.manyun.admin.domain.dto;

import com.manyun.admin.domain.vo.CntCollectionAlterVo;
import com.manyun.admin.domain.vo.CntCollectionInfoAlterVo;
import com.manyun.admin.domain.vo.CntLableAlterVo;
import com.manyun.admin.domain.vo.MediaAlterVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("藏品改动参数组合视图")
@Data
public class CntCollectionAlterCombineDto {

    @ApiModelProperty("藏品")
    private CntCollectionAlterVo cntCollectionAlterVo;

    @ApiModelProperty("藏品详情")
    private CntCollectionInfoAlterVo cntCollectionInfoAlterVo;

    @ApiModelProperty("发行方")
    private String issuanceId;

    @ApiModelProperty("标签")
    private CntLableAlterVo cntLableAlterVo;

    @ApiModelProperty("图片")
    private MediaAlterVo mediaAlterVo;

}
