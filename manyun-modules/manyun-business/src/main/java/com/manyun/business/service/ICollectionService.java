package com.manyun.business.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.business.domain.entity.CntCollection;
import com.manyun.business.domain.query.CollectionQuery;
import com.manyun.business.domain.vo.CollectionAllVo;
import com.manyun.business.domain.vo.CollectionVo;

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

    List<CollectionVo> pageQueryList(CollectionQuery collectionQuery);

    CollectionAllVo info(String id);
}
