package com.manyun.admin.domain.dto;

import com.manyun.admin.domain.vo.CntBoxCollectionVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("盲盒中的物品请求参数")
public class SaveBoxCollectionDto {

    @ApiModelProperty("盲盒编号")
    private String boxId;

    @ApiModelProperty("盲盒中的物品相关数据")
    private List<CntBoxCollectionVo> cntBoxCollectionVos;

}
