package com.manyun.business.domain.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@ApiModel("转赠提交表单")
public class TranAccForm implements Serializable {

    @ApiModelProperty("转赠类型 0藏品，1盲盒")
    @Range(min = 0L,max = 1L,message = "转赠类型有误")
    private Integer type;

    @ApiModelProperty("(用户拥有藏品返回视图|我的盲盒基本信息) 两个视图中的 id")
    private String buiId;

    @ApiModelProperty("手机号|区块链地址|ID")
    @NotBlank(message = "手机号|区块链地址|ID不可为空")
    private String commUni;



}
