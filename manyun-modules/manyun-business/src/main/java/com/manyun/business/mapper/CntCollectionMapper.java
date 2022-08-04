package com.manyun.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.business.domain.entity.CntCollection;
import com.manyun.business.domain.query.CollectionQuery;
import com.manyun.business.domain.vo.CollectionVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 藏品表 Mapper 接口
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
public interface CntCollectionMapper extends BaseMapper<CntCollection> {

    List<CollectionVo> pageQueryList(CollectionQuery collectionQuery);

    int updateLock(@Param("id") String id,@Param("sellNum") Integer sellNum);

}
