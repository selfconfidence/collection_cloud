package com.manyun.admin.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("空投返回上链数据视图")
@Data
public class AirdropVo {

        @ApiModelProperty("用户id")
        private String userId;

        @ApiModelProperty("用户和藏品表主键")
        private String usercollectionId;

}
