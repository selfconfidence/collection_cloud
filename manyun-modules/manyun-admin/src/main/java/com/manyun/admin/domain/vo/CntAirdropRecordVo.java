package com.manyun.admin.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@ApiModel("空投记录对象返回视图")
@Data
public class CntAirdropRecordVo
{

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("用户手机号")
    private String userPhone;

    @ApiModelProperty("藏品名称")
    private String collectionName;

    @ApiModelProperty("藏品主图")
    private List<MediaVo> mediaVos;

    @ApiModelProperty("创建时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

}
