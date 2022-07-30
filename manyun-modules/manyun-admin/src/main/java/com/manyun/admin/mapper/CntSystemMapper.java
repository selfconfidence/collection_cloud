package com.manyun.admin.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.admin.domain.CntSystem;
import com.manyun.admin.domain.query.SystemQuery;

/**
 * 平台规则Mapper接口
 *
 * @author yanwei
 * @date 2022-07-28
 */
public interface CntSystemMapper extends BaseMapper<CntSystem>
{
    /**
     * 查询平台规则列表
     *
     * @param SystemQuery
     * @return 平台规则集合
     */
    public List<CntSystem> selectCntSystemList(SystemQuery SystemQuery);
}
