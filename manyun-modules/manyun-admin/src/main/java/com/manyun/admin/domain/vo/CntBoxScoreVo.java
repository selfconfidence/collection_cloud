package com.manyun.admin.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;

@ApiModel("盲盒评分对象视图")
@Data
public class CntBoxScoreVo
{

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("评分名称")
    private String scoreName;

    @ApiModelProperty("状态 0:启用 1:停用")
    private String scoreStatus;

    @ApiModelProperty("排序")
    private Integer scoreSort;

    @ApiModelProperty("创建时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    @ApiModelProperty("更新时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;

}
