package com.manyun.admin.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.admin.domain.CntActionCollection;
import com.manyun.admin.domain.query.ActionCollectionQuery;

/**
 * 活动合成目标藏品Mapper接口
 *
 * @author yanwei
 * @date 2022-09-06
 */
public interface CntActionCollectionMapper extends BaseMapper<CntActionCollection>
{
    /**
     * 查询活动合成目标藏品列表
     *
     * @param actionCollectionQuery
     * @return 活动合成目标藏品集合
     */
    public List<CntActionCollection> selectCntActionCollectionList(ActionCollectionQuery actionCollectionQuery);
}
