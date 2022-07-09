package com.manyun.business.domain.query;

import com.manyun.common.core.web.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("公告查询条件")
public class NoticeQuery extends PageQuery {



    @ApiModelProperty("公告类型（1通知 2公告）")
    private Integer noticeType;
}
