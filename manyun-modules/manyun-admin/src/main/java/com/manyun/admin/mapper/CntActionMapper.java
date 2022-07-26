package com.manyun.admin.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.admin.domain.CntAction;
import com.manyun.admin.domain.query.ActionQuery;

/**
 * 活动Mapper接口
 *
 * @author yanwei
 * @date 2022-07-21
 */
public interface CntActionMapper extends BaseMapper<CntAction>
{

    /**
     * 根据条件查询活动列表
     *
     * @param actionQuery
     * @return 活动集合
     */
    public List<CntAction> selectSearchActionList(ActionQuery actionQuery);

}
