package com.manyun.admin.domain.query;

import com.manyun.common.core.web.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.HashMap;
import java.util.Map;

@Data
@ApiModel("规定时间指定藏品买卖查询对象")
public class CollectionSalesStatisticsQuery extends PageQuery {

    @ApiModelProperty("藏品id")
    @NotBlank(message = "请选择藏品")
    private String collectionId;

    // 1 卖 2 买
    @ApiModelProperty("买卖状态")
    @Min(value = 1L)
    @Max(value = 2L)
    @NotNull(message = "请选择买卖状态")
    private Integer status;

    @ApiModelProperty("时间范围")
    private Map<String, Object> params;

    public Map<String, Object> getParams()
    {
        if (params == null)
        {
            params = new HashMap<>();
        }
        return params;
    }

    public void setParams(Map<String, Object> params)
    {
        this.params = params;
    }

}
