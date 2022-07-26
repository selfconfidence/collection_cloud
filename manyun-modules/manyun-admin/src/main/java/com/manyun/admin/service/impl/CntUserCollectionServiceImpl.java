package com.manyun.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manyun.admin.mapper.CntUserCollectionMapper;
import com.manyun.admin.domain.CntUserCollection;
import com.manyun.admin.service.ICntUserCollectionService;

/**
 * 用户购买藏品中间Service业务层处理
 *
 * @author yanwei
 * @date 2022-07-26
 */
@Service
public class CntUserCollectionServiceImpl extends ServiceImpl<CntUserCollectionMapper,CntUserCollection> implements ICntUserCollectionService
{
    @Autowired
    private CntUserCollectionMapper cntUserCollectionMapper;

}
