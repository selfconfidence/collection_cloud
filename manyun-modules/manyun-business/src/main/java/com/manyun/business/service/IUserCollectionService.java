package com.manyun.business.service;

import com.manyun.business.domain.entity.UserCollection;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户购买藏品中间表 服务类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-21
 */
public interface IUserCollectionService extends IService<UserCollection> {

    void bindCollection(String userId, String buiId, String info, Integer goodsNum);

}
