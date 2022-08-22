package com.manyun.admin.domain.dto;

import com.manyun.admin.domain.vo.CntPostSellVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("提前购的商品配置方式请求参数")
public class SavePostSellDto {

    @ApiModelProperty("配置id")
    private String configId;

    @ApiModelProperty("提前购的商品配置方式相关数据")
    private List<CntPostSellVo> cntPostSellVoList;

}
