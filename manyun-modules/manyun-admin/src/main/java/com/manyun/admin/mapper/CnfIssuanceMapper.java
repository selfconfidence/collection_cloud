package com.manyun.admin.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.admin.domain.CnfIssuance;
import com.manyun.admin.domain.query.IssuanceQuery;

/**
 * 发行方Mapper接口
 *
 * @author yanwei
 * @date 2022-08-09
 */
public interface CnfIssuanceMapper extends BaseMapper<CnfIssuance>
{
    /**
     * 查询发行方列表
     *
     * @param issuanceQuery
     * @return 发行方集合
     */
    public List<CnfIssuance> selectCnfIssuanceList(IssuanceQuery issuanceQuery);
}
