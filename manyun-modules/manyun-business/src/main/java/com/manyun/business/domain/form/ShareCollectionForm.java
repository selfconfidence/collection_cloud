package com.manyun.business.domain.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("分享藏品提交参数")
public class ShareCollectionForm {

    @ApiModelProperty("我的藏品id（我拥有的）")
    @NotBlank(message = "id不可为空")
    private String myGoodsId;
}
