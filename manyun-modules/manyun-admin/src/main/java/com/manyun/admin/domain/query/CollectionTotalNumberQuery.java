package com.manyun.admin.domain.query;

import com.manyun.common.core.web.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.HashMap;
import java.util.Map;

@ApiModel("规定时间指定一件或多件藏品的持有总量查询对象")
public class CollectionTotalNumberQuery extends PageQuery {

    @ApiModelProperty("藏品ids")
    private String[] collectionIds;

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

    public String[] getCollectionIds() {
        return collectionIds;
    }

    public void setCollectionIds(String[] collectionIds) {
        this.collectionIds = collectionIds;
    }
}
