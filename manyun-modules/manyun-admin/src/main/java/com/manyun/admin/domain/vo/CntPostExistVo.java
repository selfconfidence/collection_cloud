package com.manyun.admin.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;

@ApiModel("提前购配置已经拥有对象")
@Data
public class CntPostExistVo
{

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("藏品编号")
    private String collectionId;

    @ApiModelProperty("配置编号")
    private String configId;

    @ApiModelProperty("业务名称")
    private String buiName;

    @ApiModelProperty("创建时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    @ApiModelProperty("更新时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;

}
