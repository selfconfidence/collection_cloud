package com.manyun.admin.domain.dto;

import com.manyun.admin.domain.vo.CntPostExistVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("提前购配置方式前置条件请求参数")
public class SavePostExistDto {

    @ApiModelProperty("配置id")
    private String configId;

    @ApiModelProperty("配置方式前置条件相关数据")
    private List<CntPostExistVo> cntPostExistVoList;

}
