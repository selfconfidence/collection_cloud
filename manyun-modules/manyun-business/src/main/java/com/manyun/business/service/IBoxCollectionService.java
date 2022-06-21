package com.manyun.business.service;

import com.manyun.business.domain.entity.BoxCollection;
import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.business.domain.vo.BoxCollectionJoinVo;

import java.util.List;

/**
 * <p>
 * 盲盒与藏品中间表 服务类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
public interface IBoxCollectionService extends IService<BoxCollection> {

    /**
     * 盲盒编号
     * @param boxId
     * @return
     */
    List<BoxCollectionJoinVo> findJoinCollections(String boxId);
}
