package com.manyun.admin.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.admin.domain.CntCollection;
import com.manyun.admin.domain.query.CollectionQuery;
import com.manyun.admin.domain.vo.CntCollectionDetailsVo;
import org.apache.ibatis.annotations.Param;

/**
 * 藏品Mapper接口
 *
 * @author yanwei
 * @date 2022-07-14
 */
public interface CntCollectionMapper extends BaseMapper<CntCollection>
{

    /**
     * 根据条件查询藏品列表
     *
     * @param collectionQuery
     * @return 藏品集合
     */
    public List<CntCollection> selectSearchCollectionList(CollectionQuery collectionQuery);

    /**
     * 查询藏品详情
     *
     * @param id 藏品主键
     * @return 藏品
     */
    CntCollectionDetailsVo selectCntCollectionDetailsById(String id);

}
