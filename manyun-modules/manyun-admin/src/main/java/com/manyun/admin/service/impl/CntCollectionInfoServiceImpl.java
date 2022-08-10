package com.manyun.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

}
