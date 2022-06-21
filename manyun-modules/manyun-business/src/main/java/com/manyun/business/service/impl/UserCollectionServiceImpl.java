package com.manyun.business.service.impl;

import cn.hutool.core.util.IdUtil;
import com.google.common.collect.Lists;
import com.manyun.business.domain.dto.LogInfoDto;
import com.manyun.business.domain.entity.UserCollection;
import com.manyun.business.mapper.UserCollectionMapper;
import com.manyun.business.service.ILogsService;
import com.manyun.business.service.IUserCollectionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.common.core.domain.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import static com.manyun.common.core.constant.BusinessConstants.LogsTypeConstant.PULL_SOURCE;
import static com.manyun.common.core.constant.BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE;
import static com.manyun.common.core.constant.BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE;
import static com.manyun.common.core.enums.CollectionLink.NOT_LINK;

/**
 * <p>
 * 用户购买藏品中间表 服务实现类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-21
 */
@Service
public class UserCollectionServiceImpl extends ServiceImpl<UserCollectionMapper, UserCollection> implements IUserCollectionService {


    @Autowired
    private ILogsService logsService;

    /**
     * 绑定用户与藏品的相关联信息
     * @param userId
     * @param buiId
     * @param info
     * @param goodsNum
     */
    @Override
    public void bindCollection(String userId, String buiId, String info, Integer goodsNum) {
        ArrayList<UserCollection> userCollections = Lists.newArrayList();
        for (Integer i = 0; i < goodsNum; i++) {
            UserCollection userCollection = Builder.of(UserCollection::new).build();
            userCollection.setId(IdUtil.getSnowflake().nextIdStr());
            userCollection.setCollectionId(buiId);
            userCollection.setUserId(userId);
            userCollection.setSourceInfo(info);
            // 初始化 未上链过程
            userCollection.setIsLink(NOT_LINK.getCode());
            userCollection.createD(userId);
            userCollections.add(userCollection);
        }
        saveBatch(userCollections);
        // 增加日志
        logsService.saveLogs(LogInfoDto.builder().jsonTxt(info).buiId(userId).modelType(COLLECTION_MODEL_TYPE).isType(PULL_SOURCE).formInfo(goodsNum.toString()).build());
    }
}
