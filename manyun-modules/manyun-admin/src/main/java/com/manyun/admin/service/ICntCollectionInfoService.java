package com.manyun.admin.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.admin.domain.CntCollectionInfo;
import com.manyun.admin.domain.query.CollectionInfoQuery;
import com.manyun.admin.domain.vo.CntCollectionInfoVo;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * 藏品详情Service接口
 *
 * @author yanwei
 * @date 2022-07-14
 */
public interface ICntCollectionInfoService extends IService<CntCollectionInfo>
{

    /**
     * 查询藏品详情列表
     *
     * @param collectionInfoQuery
     * @return 藏品详情集合
     */
    public TableDataInfo<CntCollectionInfoVo> selectCntCollectionInfoList(CollectionInfoQuery collectionInfoQuery);

}
