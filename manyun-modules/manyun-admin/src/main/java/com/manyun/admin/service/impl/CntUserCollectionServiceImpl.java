package com.manyun.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.admin.domain.query.CollectionTotalNumberQuery;
import com.manyun.admin.domain.query.UserCollectionNumberQuery;
import com.manyun.admin.domain.vo.*;
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

    /**
     * 重试上链列表
     */
    @Override
    public List<MyChainxVo> myChainxList() {
        return cntUserCollectionMapper.myChainxList();
    }

    /**
     * 规定时间指定一件或多件藏品的持有总量查询
     * @param collectionTotalNumberQuery
     * @return
     */
    @Override
    public List<CollectionTotalNumberVo> selectCollectionTotalNumber(CollectionTotalNumberQuery collectionTotalNumberQuery) {
        return cntUserCollectionMapper.selectCollectionTotalNumber(collectionTotalNumberQuery.getCollectionIds(),collectionTotalNumberQuery.getParams());
    }


    /**
     * 每个用户藏品总量查询
     * @return
     */
    @Override
    public List<UserCollectionNumberVo> userCollectionNumber(UserCollectionNumberQuery userCollectionNumberQuery) {
        return cntUserCollectionMapper.userCollectionNumber(userCollectionNumberQuery);
    }

    /**
     * 查询满足条件的用户ids
     * @param goodId
     * @param count
     * @return
     */
    @Override
    public List<String> selectMeetTheConditionsUserIds(String goodId, Integer count) {
        return cntUserCollectionMapper.selectMeetTheConditionsUserIds(goodId,count);
    }

    /**
     * 查询满足条件的数据
     * @return
     */
    @Override
    public List<GoodsVo> selectMeetTheConditionsData(String userId,List<String> goodIds) {
        return cntUserCollectionMapper.selectMeetTheConditionsData(userId,goodIds);
    }

}
