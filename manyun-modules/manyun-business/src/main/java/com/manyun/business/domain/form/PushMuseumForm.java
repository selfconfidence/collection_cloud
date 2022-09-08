package com.manyun.business.domain.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("3d展馆提交参数")
public class PushMuseumForm {

    @ApiModelProperty("我的藏品id集合")
    private List<String> collections;
}
