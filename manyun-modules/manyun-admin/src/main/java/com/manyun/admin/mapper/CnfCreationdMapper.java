package com.manyun.admin.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.admin.domain.CnfCreationd;
import com.manyun.admin.domain.query.CreationdQuery;

/**
 * 创作者Mapper接口
 *
 * @author yanwei
 * @date 2022-07-13
 */
public interface CnfCreationdMapper extends BaseMapper<CnfCreationd>
{

    /**
     * 根据条件查询创作者列表
     * @param creationdQuery
     * @return
     */
    List<CnfCreationd> selectSerachCreationdList(CreationdQuery creationdQuery);

}
