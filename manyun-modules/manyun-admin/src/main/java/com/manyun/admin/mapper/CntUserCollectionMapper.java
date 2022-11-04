package com.manyun.admin.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.admin.domain.CntUserCollection;
import com.manyun.admin.domain.query.CollectionTotalNumberQuery;
import com.manyun.admin.domain.query.UserCollectionNumberQuery;
import com.manyun.admin.domain.vo.*;
import org.apache.ibatis.annotations.Param;

/**
 * 用户购买藏品中间Mapper接口
 *
 * @author yanwei
 * @date 2022-07-22
 */
public interface CntUserCollectionMapper  extends BaseMapper<CntUserCollection>
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
     * @param collectionIds
     * @param params
     * @return
     */
    List<CollectionTotalNumberVo> selectCollectionTotalNumber(@Param("collectionIds") String[] collectionIds,@Param("params") Map<String, Object> params);

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
    List<String> selectMeetTheConditionsUserIds(@Param("goodId") String goodId,@Param("count") Integer count);

    /**
     * 查询满足条件的数据
     * @return
     */
    List<GoodsVo> selectMeetTheConditionsData(@Param("userId") String userId,@Param("goodIds") List<String> goodIds);

    List<CollectionNumberVo> collectionNumber(@Param("goodsId") String goodId);
}
