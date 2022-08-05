package com.manyun.business.service;

import com.manyun.business.domain.dto.UserCollectionCountDto;
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

    UserCollectionVo userCollectionById(String id);

    Boolean existUserCollection(String userId, String id);

    void tranCollection(String tranUserId, String toUserId, String buiId);

    List<UserCollectionCountDto> selectUserCollectionCount(List<String> collectionIds, String userId);

    void deleteMaterial(String userId,String collectionId, Integer deleteNum);


    String hideUserCollection(String buiId, String userId, String info);

    void resetUpLink(String userId, String userCollectionId);

    String showUserCollection(String userId, String buiId, String info);

}
