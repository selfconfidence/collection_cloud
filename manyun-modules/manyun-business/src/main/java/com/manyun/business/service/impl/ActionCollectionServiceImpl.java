package com.manyun.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.business.domain.entity.ActionCollection;
import com.manyun.business.mapper.ActionCollectionMapper;
import com.manyun.business.service.IActionCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 活动合成目标藏品Service业务层处理
 *
 * @author yanwei
 * @date 2022-09-06
 */
@Service
public class ActionCollectionServiceImpl extends ServiceImpl<ActionCollectionMapper, ActionCollection> implements IActionCollectionService
{
    @Autowired
    private ActionCollectionMapper actionCollectionMapper;

}
