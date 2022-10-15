package com.manyun.admin.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.admin.domain.CntOpinion;
import com.manyun.admin.domain.query.OpinionQuery;

/**
 * 产品建议Mapper接口
 *
 * @author yanwei
 * @date 2022-07-26
 */
public interface CntOpinionMapper extends BaseMapper<CntOpinion>
{
    /**
     * 查询产品建议列表
     *
     * @param opinionQuery 产品建议
     * @return 产品建议集合
     */
    public List<CntOpinion> selectCntOpinionList(OpinionQuery opinionQuery);
}
