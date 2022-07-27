package com.manyun.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.admin.domain.vo.UserCollectionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manyun.admin.mapper.CntUserCollectionMapper;
import com.manyun.admin.domain.CntUserCollection;
import com.manyun.admin.service.ICntUserCollectionService;

import java.util.List;

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

    /**
     * 我的藏品
     */
    @Override
    public List<UserCollectionVo> myCollectionList(String userId) {
        return cntUserCollectionMapper.myCollectionList(userId);
    }

}
