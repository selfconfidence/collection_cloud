package com.manyun.admin.domain.query;

import com.manyun.common.core.web.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@ApiModel("用户抽签购买藏品或盲盒中间对象条件查询")
@Data
public class UserTarQuery extends PageQuery
{

    @ApiModelProperty("抽签编号")
    private String tarId;

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("是否购买，1=未购买,2=已购买")
    private Integer goSell;

    @ApiModelProperty("1=已抽中,2=未抽中,4等待开奖")
    private Integer isFull;

}
