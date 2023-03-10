package com.manyun.business.service;

import com.manyun.business.domain.dto.UserCollectionCountDto;
import com.manyun.business.domain.entity.UserCollection;
import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.business.domain.form.PushMuseumForm;
import com.manyun.business.domain.vo.ChainxLinkVo;
import com.manyun.business.domain.vo.MuseumListVo;
import com.manyun.business.domain.vo.UserCollectionVo;
import com.manyun.common.core.domain.R;

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

    String bindOrderCollection(String userId, String buiId, String collectionName, String info, Integer goodsNum);

    void bindCollection(String userId, String buiId, String collectionName, String info);


    String autoCollectionNum(String collectionId);

    List<UserCollectionVo> userCollectionPageList(String userId,String commName);

    UserCollectionVo userCollectionById(String id);

    Boolean existUserCollection(String userId, String id);

    String tranCollection(String tranUserId, String toUserId, String buiId);

    List<UserCollectionCountDto> selectUserCollectionCount(List<String> collectionIds, String userId);

    void deleteMaterial(String userId,String collectionId, Integer deleteNum);

    void updateMaterial(String userId,String collectionId, Integer deleteNum);

    String hideUserCollection(String buiId, String userId, String info);

    void resetUpLink(String userId, String userCollectionId);

    String showUserCollection(String userId, String buiId, String info);

    R<String> shareCollection(String userId, String myGoodsId);

    void pushMuseum(PushMuseumForm pushMuseumForm, String userId);

    List<MuseumListVo> museumList(String userId);

    ChainxLinkVo getUserLinkAddr(String linkAddr);

//    String showUserCollection(String buiId, String userId, String info);
}
