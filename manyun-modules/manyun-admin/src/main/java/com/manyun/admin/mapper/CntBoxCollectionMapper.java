package com.manyun.admin.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.admin.domain.CntBoxCollection;
import com.manyun.admin.domain.query.BoxCollectionQuery;
import org.apache.ibatis.annotations.Param;

/**
 * 盲盒与藏品中间Mapper接口
 *
 * @author yanwei
 * @date 2022-07-15
 */
public interface CntBoxCollectionMapper extends BaseMapper<CntBoxCollection>
{

    /**
     * 查询盲盒与藏品中间列表
     *
     * @param boxCollectionQuery 盲盒与藏品中间
     * @return 盲盒与藏品中间集合
     */
    public List<CntBoxCollection> selectSearchBoxCollectionList(BoxCollectionQuery boxCollectionQuery);

}
