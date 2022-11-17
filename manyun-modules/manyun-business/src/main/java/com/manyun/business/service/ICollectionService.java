package com.manyun.business.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.business.domain.entity.CntCollection;
import com.manyun.business.domain.form.CollectionOrderSellForm;
import com.manyun.business.domain.form.CollectionSellForm;
import com.manyun.business.domain.query.CollectionQuery;
import com.manyun.business.domain.query.UseAssertQuery;
import com.manyun.business.domain.vo.*;
import com.manyun.comm.api.domain.redis.collection.CollectionAllRedisVo;
import com.manyun.comm.api.domain.redis.collection.CollectionRedisVo;
import com.manyun.common.core.web.page.PageQuery;
import com.manyun.common.core.web.page.TableDataInfo;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 藏品表 服务类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
public interface ICollectionService extends IService<CntCollection> {

    TableDataInfo<CollectionVo> pageQueryList(CollectionQuery collectionQuery);

    CollectionAllRedisVo infoCache(String id, String userId);

    CollectionAllVo info(String id, String userId);

    CollectionAllVo info(String id);

    PayVo sellCollection(String userId, CollectionSellForm collectionSellForm);

    TableDataInfo<UserCollectionVo> userCollectionPageList(UseAssertQuery useAssertQuery, String userId);

    List<UserCateVo> cateCollectionByUserId(String userId);

    List<KeywordVo> queryDict(String keyword);

    String tarCollection(String id, String userId);

    CollectionVo getBaseCollectionVo(@NotNull String collectionId);

    CollectionVo getSoleBaseCollectionVo(@NotNull String collectionId);

    UserCollectionForVo userCollectionInfo(String id);

    CollectionOrderAllVo orderInfo(String id);

    List<CateCollectionVo> cateCollectionChildList(String userId,String cateParentId);

    String sellOrderCollection(String userId, CollectionOrderSellForm collectionOrderSellForm);


    void checkBalance(String cntCollectionId, Integer sellNum);

    List<KeywordVo> thisAssertQueryDict(String userId, String keyword);

    TableDataInfo<CollectionVo> homeCacheList();

    List<StepVo> initStepVo(String linkAddr, String collectionModelType);
}
