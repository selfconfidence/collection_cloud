package com.manyun.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manyun.admin.mapper.CntCollectionLableMapper;
import com.manyun.admin.domain.CntCollectionLable;
import com.manyun.admin.service.ICntCollectionLableService;

/**
 * 藏品和标签中间（必须选定个标签,最多为三个）Service业务层处理
 *
 * @author yanwei
 * @date 2022-07-25
 */
@Service
public class CntCollectionLableServiceImpl extends ServiceImpl<CntCollectionLableMapper,CntCollectionLable> implements ICntCollectionLableService
{
    @Autowired
    private CntCollectionLableMapper cntCollectionLableMapper;

}
