package com.manyun.admin.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.admin.domain.CntTar;

/**
 * 抽签规则(盲盒,藏品)Mapper接口
 *
 * @author yanwei
 * @date 2022-07-27
 */
public interface CntTarMapper extends BaseMapper<CntTar>
{
    /**
     * 查询抽签规则(盲盒,藏品)列表
     *
     * @param cntTar 抽签规则(盲盒,藏品)
     * @return 抽签规则(盲盒,藏品)集合
     */
    public List<CntTar> selectCntTarList(CntTar cntTar);
}
