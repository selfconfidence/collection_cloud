package com.manyun.admin.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.manyun.admin.domain.query.CollectionInfoQuery;
import com.manyun.admin.domain.vo.CntCollectionInfoVo;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manyun.admin.mapper.CntCollectionInfoMapper;
import com.manyun.admin.domain.CntCollectionInfo;
import com.manyun.admin.service.ICntCollectionInfoService;

/**
 * 藏品详情Service业务层处理
 *
 * @author yanwei
 * @date 2022-07-14
 */
@Service
public class CntCollectionInfoServiceImpl extends ServiceImpl<CntCollectionInfoMapper,CntCollectionInfo> implements ICntCollectionInfoService
{
    @Autowired
    private CntCollectionInfoMapper cntCollectionInfoMapper;

    /**
     * 查询藏品详情列表
     *
     * @param collectionInfoQuery
     * @return 藏品详情
     */
    @Override
    public TableDataInfo<CntCollectionInfoVo> selectCntCollectionInfoList(CollectionInfoQuery collectionInfoQuery)
    {
        PageHelper.startPage(collectionInfoQuery.getPageNum(),collectionInfoQuery.getPageSize());
        List<CntCollectionInfoVo> cntCollectionInfoVos = cntCollectionInfoMapper.selectCollectionRelatedInfoList(collectionInfoQuery);
        return TableDataInfoUtil.pageTableDataInfo(cntCollectionInfoVos,cntCollectionInfoVos);
    }

}
