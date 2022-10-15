package com.manyun.admin.domain.query;

import com.manyun.common.core.web.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel("产品举报反馈条件查询对象")
public class FeedbackQuery extends PageQuery {

    @ApiModelProperty("手机号")
    private String feedbackUserPhone;

}
