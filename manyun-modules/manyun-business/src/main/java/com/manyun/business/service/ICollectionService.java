package com.manyun.business.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.business.domain.entity.CntCollection;
import com.manyun.business.domain.form.CollectionSellForm;
import com.manyun.business.domain.query.CollectionQuery;
import com.manyun.business.domain.vo.*;
import com.manyun.common.core.web.page.PageQuery;
import com.manyun.common.core.web.page.TableDataInfo;

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

    CollectionAllVo info(String id,String userId);

    CollectionAllVo info(String id);

    PayVo sellCollection(String userId, CollectionSellForm collectionSellForm);

    TableDataInfo<UserCollectionVo> userCollectionPageList(PageQuery pageQuery, String userId);

    List<UserCateVo> cateCollectionByUserId(String userId);

    List<String> queryDict(String keyword);

    Integer tarCollection(String id, String userId);
}
