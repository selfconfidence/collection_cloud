package com.manyun.admin.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.admin.domain.CntPostExist;

/**
 * 提前购配置已经拥有Mapper接口
 *
 * @author yanwei
 * @date 2022-07-27
 */
public interface CntPostExistMapper extends BaseMapper<CntPostExist>
{
    /**
     * 查询提前购配置已经拥有列表
     *
     * @param cntPostExist 提前购配置已经拥有
     * @return 提前购配置已经拥有集合
     */
    public List<CntPostExist> selectCntPostExistList(CntPostExist cntPostExist);
}
