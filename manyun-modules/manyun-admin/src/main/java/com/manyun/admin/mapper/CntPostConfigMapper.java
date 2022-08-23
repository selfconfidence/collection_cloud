package com.manyun.admin.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.admin.domain.CntPostConfig;
import com.manyun.admin.domain.query.PostConfigQuery;

/**
 * 提前购配置-只能有一条Mapper接口
 *
 * @author yanwei
 * @date 2022-08-15
 */
public interface CntPostConfigMapper extends BaseMapper<CntPostConfig>
{
    /**
     * 查询提前购配置-只能有一条列表
     *
     * @param postConfigQuery
     * @return 提前购配置-只能有一条集合
     */
    public List<CntPostConfig> selectCntPostConfigList(PostConfigQuery postConfigQuery);
}
