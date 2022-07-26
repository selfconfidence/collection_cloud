package com.manyun.admin.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.admin.domain.CntCollectionInfo;
import com.manyun.admin.domain.query.CollectionInfoQuery;
import com.manyun.admin.domain.vo.CntCollectionInfoVo;

/**
 * 藏品详情Mapper接口
 *
 * @author yanwei
 * @date 2022-07-14
 */
public interface CntCollectionInfoMapper extends BaseMapper<CntCollectionInfo>
{

    /**
     * 查询藏品相关信息列表
     *
     * @param collectionInfoQuery
     * @return 发行方返回视图
     */
    List<CntCollectionInfoVo> selectCollectionRelatedInfoList(CollectionInfoQuery collectionInfoQuery);

}
