package com.manyun.admin.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.admin.domain.CntBoxScore;
import com.manyun.admin.domain.query.BoxScoreQuery;

/**
 * 盲盒评分Mapper接口
 *
 * @author yanwei
 * @date 2022-08-19
 */
public interface CntBoxScoreMapper extends BaseMapper<CntBoxScore>
{
    /**
     * 查询盲盒评分列表
     *
     * @param boxScoreQuery 盲盒评分
     * @return 盲盒评分集合
     */
    public List<CntBoxScore> selectCntBoxScoreList(BoxScoreQuery boxScoreQuery);
}
