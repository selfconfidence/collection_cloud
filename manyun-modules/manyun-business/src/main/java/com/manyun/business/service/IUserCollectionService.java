package com.manyun.business.service;

import com.manyun.business.domain.entity.UserCollection;
import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.business.domain.vo.UserCollectionVo;

import java.util.List;

/**
 * <p>
 * 用户购买藏品中间表 服务类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-21
 */
public interface IUserCollectionService extends IService<UserCollection> {

    void bindCollection(String userId, String buiId, String collectionName,String info, Integer goodsNum);

    List<UserCollectionVo> userCollectionPageList(String userId);

    Boolean existUserCollection(String userId, String id);

    void tranCollection(String tranUserId, String toUserId, String buiId);
}
