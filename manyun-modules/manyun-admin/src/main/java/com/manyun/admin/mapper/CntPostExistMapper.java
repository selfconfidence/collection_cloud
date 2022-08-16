package com.manyun.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.admin.domain.CntPostExist;
import com.manyun.admin.domain.query.PostExistQuery;
import com.manyun.admin.domain.vo.CntPostExistVo;

import java.util.List;

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
     * @param postExistQuery
     * @return 提前购配置已经拥有集合
     */
    public List<CntPostExistVo> selectCntPostExistList(PostExistQuery postExistQuery);
}
