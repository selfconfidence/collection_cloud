package com.manyun.business.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.manyun.business.domain.dto.LogInfoDto;
import com.manyun.business.domain.dto.StepDto;
import com.manyun.business.domain.dto.UserCollectionCountDto;
import com.manyun.business.domain.entity.UserCollection;
import com.manyun.business.domain.vo.UserCollectionVo;
import com.manyun.business.mapper.UserCollectionMapper;
import com.manyun.business.service.ILogsService;
import com.manyun.business.service.IStepService;
import com.manyun.business.service.IUserCollectionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.common.core.domain.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.manyun.common.core.constant.BusinessConstants.LogsTypeConstant.POLL_SOURCE;
import static com.manyun.common.core.constant.BusinessConstants.LogsTypeConstant.PULL_SOURCE;
import static com.manyun.common.core.constant.BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE;
import static com.manyun.common.core.constant.BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE;
import static com.manyun.common.core.enums.CollectionLink.NOT_LINK;
import static com.manyun.common.core.enums.CollectionLink.OK_LINK;

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

    @Resource
    private UserCollectionMapper userCollectionMapper;

    @Autowired
    private IStepService stepService;

    /**
     * 绑定用户与藏品的相关联信息
     * @param userId
     * @param buiId
     * @param info
     * @param goodsNum
     */
    @Override
    public void bindCollection(String userId, String buiId, String collectionName,String info, Integer goodsNum) {
        ArrayList<UserCollection> userCollections = Lists.newArrayList();
        for (Integer i = 0; i < goodsNum; i++) {
            UserCollection userCollection = Builder.of(UserCollection::new).build();
            userCollection.setId(IdUtil.getSnowflake().nextIdStr());
            userCollection.setCollectionId(buiId);
            userCollection.setUserId(userId);
            userCollection.setSourceInfo(info);
            userCollection.setCollectionName(collectionName);
            // 初始化 未上链过程
            userCollection.setIsLink(NOT_LINK.getCode());
            userCollection.createD(userId);
            userCollections.add(userCollection);
        }
        // TODO 上链
        saveBatch(userCollections);
        // 增加日志
        logsService.saveLogs(LogInfoDto.builder().jsonTxt(info).buiId(userId).modelType(COLLECTION_MODEL_TYPE).isType(PULL_SOURCE).formInfo(goodsNum.toString()).build());
    }

    /**
     * 绑定盲盒
     * @param userId
     * @return
     */

    @Override
    public List<UserCollectionVo> userCollectionPageList(String userId) {
        return userCollectionMapper.userCollectionPageList(userId);
    }

    /**
     * 是否存在该藏品?
     * @param userId
     * @param id
     * @return
     */
    @Override
    public Boolean existUserCollection(String userId, String id) {
        // 必须是已上链的
        return Objects.nonNull(getOne(Wrappers.<UserCollection>lambdaQuery().eq(UserCollection::getId,id).eq(UserCollection::getUserId,userId).eq(UserCollection::getIsLink,OK_LINK)));
    }

    /**
     * 转让 藏品
     * @param tranUserId
     * @param toUserId
     * @param buiId
     */
    @Override
    public void tranCollection(String tranUserId, String toUserId, String buiId) {
        UserCollection userCollection = getOne(Wrappers.<UserCollection>lambdaQuery().eq(UserCollection::getId, buiId).eq(UserCollection::getUserId, tranUserId).eq(UserCollection::getIsLink, OK_LINK));
        String format = StrUtil.format("{}:赠送获得该藏品!", userCollection.getCollectionName());
        String formatTran = StrUtil.format("{}:藏品被赠送!",userCollection.getCollectionName());
        //TODO 转让还是重新上链?
        bindCollection(toUserId,userCollection.getCollectionId(),userCollection.getCollectionName(),format,Integer.valueOf(1));
        //删除原始拥有者的绑定关系
        removeById(userCollection.getId());
        // 增加日志 ...................
        logsService.saveLogs(
                LogInfoDto.builder().jsonTxt(format).buiId(toUserId).modelType(COLLECTION_MODEL_TYPE).isType(PULL_SOURCE).formInfo(Integer.valueOf(1).toString()).build()
                ,LogInfoDto.builder().jsonTxt(formatTran).buiId(tranUserId).modelType(COLLECTION_MODEL_TYPE).isType(POLL_SOURCE).formInfo(Integer.valueOf(1).toString()).build()
        );

        // 流转记录
        stepService.saveBatch(StepDto.builder().buiId(buiId).userId(tranUserId).modelType(COLLECTION_MODEL_TYPE).reMark("转让方").formInfo(formatTran).build()
                ,StepDto.builder().buiId(buiId).userId(toUserId).modelType(COLLECTION_MODEL_TYPE).reMark("受让方").formInfo(format).build()
        );


    }

    /**
     *  查询用户拥有个藏品的数量
     * @param collectionIds
     * @return
     */
    @Override
    public List<UserCollectionCountDto> selectUserCollectionCount(List<String> collectionIds, String userId) {
        return userCollectionMapper.selectUserCollectionCount(collectionIds,userId);
    }

    @Override
    public void deleteMaterial(String userId,String collectionId, Integer deleteNum) {
        userCollectionMapper.deleteMaterial(userId,collectionId,deleteNum);
    }

}
