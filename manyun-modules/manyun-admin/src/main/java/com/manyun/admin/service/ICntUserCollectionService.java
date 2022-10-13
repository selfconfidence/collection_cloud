package com.manyun.admin.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.admin.domain.CntUserCollection;
import com.manyun.admin.domain.query.CollectionTotalNumberQuery;
import com.manyun.admin.domain.query.UserCollectionNumberQuery;
import com.manyun.admin.domain.vo.*;

/**
 * 用户购买藏品中间Service接口
 *
 * @author yanwei
 * @date 2022-07-26
 */
public interface ICntUserCollectionService extends IService<CntUserCollection>
{

    /**
     * 我的藏品
     */
    List<UserCollectionVo> myCollectionList(String userId);

    /**
     * 重试上链列表
     */
    List<MyChainxVo> myChainxList();

    /**
     * 规定时间指定一件或多件藏品的持有总量查询
     * @param collectionTotalNumberQuery
     * @return
     */
    List<CollectionTotalNumberVo> selectCollectionTotalNumber(CollectionTotalNumberQuery collectionTotalNumberQuery);

    /**
     * 每个用户藏品总量查询
     * @return
     */
    List<UserCollectionNumberVo> userCollectionNumber(UserCollectionNumberQuery userCollectionNumberQuery);

    /**
     * 查询满足条件的用户ids
     * @param goodId
     * @param count
     * @return
     */
    List<String> selectMeetTheConditionsUserIds(String goodId, Integer count);

    /**
     * 查询满足条件的数据
     * @return
     */
    List<GoodsVo> selectMeetTheConditionsData(String userId,List<String> goodIds);

}
