package com.manyun.admin.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.admin.domain.CntLable;

/**
 * 藏品标签Mapper接口
 *
 * @author yanwei
 * @date 2022-07-14
 */
public interface CntLableMapper extends BaseMapper<CntLable>
{

    /**
     * 查询藏品标签列表
     *
     * @param cntLable 藏品标签
     * @return 藏品标签集合
     */
    public List<CntLable> selectCntLableList(CntLable cntLable);

}
