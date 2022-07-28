package com.manyun.business.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.manyun.business.design.mychain.MyChainService;
import com.manyun.business.domain.dto.*;
import com.manyun.business.domain.entity.UserCollection;
import com.manyun.business.domain.vo.UserCollectionVo;
import com.manyun.business.mapper.UserCollectionMapper;
import com.manyun.business.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.common.core.domain.Builder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.manyun.common.core.constant.BusinessConstants.LogsTypeConstant.POLL_SOURCE;
import static com.manyun.common.core.constant.BusinessConstants.LogsTypeConstant.PULL_SOURCE;
import static com.manyun.common.core.constant.BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE;
import static com.manyun.common.core.enums.CollectionLink.NOT_LINK;
import static com.manyun.common.core.enums.CollectionLink.OK_LINK;
import static com.manyun.common.core.enums.CommAssetStatus.NOT_EXIST;
import static com.manyun.common.core.enums.CommAssetStatus.USE_EXIST;

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

    @Autowired
    private IMsgService msgService;

    @Autowired
    private MyChainService myChainService;

    @Autowired
    private ObjectFactory<ICollectionService> collectionServiceObjectFactory;

    /**
     * 直接上链即可,转让的新增个函数进行处理
     *
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
            userCollection.setLinkAddr(IdUtil.getSnowflake().nextIdStr());
            userCollection.setIsExist(USE_EXIST.getCode());
            userCollection.setCollectionName(collectionName);
            // 初始化 未上链过程
            userCollection.setIsLink(NOT_LINK.getCode());
            userCollection.createD(userId);
            userCollections.add(userCollection);
        }
        saveBatch(userCollections);
        // 增加日志
        logsService.saveLogs(LogInfoDto.builder().jsonTxt(info).buiId(userId).modelType(COLLECTION_MODEL_TYPE).isType(PULL_SOURCE).formInfo(goodsNum.toString()).build());
        // 开始上链 // 组装所有上链所需要数据结构 并且不能报错
        for (UserCollection userCollection : userCollections) {
            BigDecimal realPrice = collectionServiceObjectFactory.getObject().getById(userCollection.getCollectionId()).getRealPrice();
            myChainService.accountCollectionUp(CallCommitDto.builder()
                    .userCollectionId(userCollection.getId())
                    .artId(userCollection.getLinkAddr())
                            .artName(userCollection.getCollectionName())
                            .artSize("80")
                            .location(userCollection.getSourceInfo())
                            .price(realPrice.toString())
                            .date(userCollection.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy MM dd")))
                            .sellway(userCollection.getSourceInfo())
                            .owner(userCollection.getUserId())
                    .build(), (hash)->{
                userCollection.setIsLink(OK_LINK.getCode());
                userCollection.setRealCompany("蚂蚁链");
                // 编号特殊生成
                userCollection.setCollectionNumber(StrUtil.format("CNT_{}",IdUtil.nanoId()));
                //userCollection.setLinkAddr(hash);
                userCollection.setCollectionHash(hash);
                userCollection.updateD(userCollection.getUserId());
                updateById(userCollection);
            });
        }
    }




    /**
     *
     * @param tranUserId 转让方用户编号
     * @param toUserId  受让方用户编号
     * @param tranUserCollection  转让方的藏品中间表
     * @param format
     * @param formatTran
     */
    // 转让,需要额外处理了,这个链 属于转增级别的链
   public void tranCollection(String tranUserId,String toUserId,UserCollection tranUserCollection,String format,String formatTran){
       UserCollection userCollection = Builder.of(UserCollection::new).build();
       userCollection.setId(IdUtil.getSnowflake().nextIdStr());
       userCollection.setCollectionId(tranUserCollection.getCollectionId());
       userCollection.setUserId(toUserId);
       userCollection.setSourceInfo(format);
       userCollection.setLinkAddr(tranUserCollection.getLinkAddr());
       userCollection.setIsExist(USE_EXIST.getCode());
       userCollection.setCollectionName(tranUserCollection.getCollectionName());
       // 初始化 未上链过程
       userCollection.setIsLink(NOT_LINK.getCode());
       userCollection.createD(toUserId);
       save(userCollection);
       // 开始转赠
       myChainService.tranForm(CallTranDto.builder()
               .userCollectionId(userCollection.getId())
               .artId(userCollection.getLinkAddr())
               .owner(userCollection.getUserId())
               .date(userCollection.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy MM dd"))).build(), (hash) ->{
           // 得到 hash
           userCollection.setIsLink(OK_LINK.getCode());
           userCollection.setRealCompany("蚂蚁链");
           // 编号特殊生成
           userCollection.setCollectionNumber(StrUtil.format("CNT_{}", IdUtil.nanoId()));
          // userCollection.setLinkAddr(hash);
           userCollection.setCollectionHash(hash);
           userCollection.updateD(userCollection.getUserId());
           updateById(userCollection);
           // 流转记录
           stepService.saveBatch(StepDto.builder().buiId(tranUserCollection.getCollectionId()).userId(tranUserId).modelType(COLLECTION_MODEL_TYPE).reMark("转让方").formHash(hash).formInfo(formatTran).build()
                   ,StepDto.builder().buiId(tranUserCollection.getCollectionId()).userId(toUserId).modelType(COLLECTION_MODEL_TYPE).formHash(hash).reMark("受让方").formInfo(format).build()
           );
       });




   }

    /**
     * 查询拥有拥有得藏品
     * @param userId
     * @return
     */

    @Override
    public List<UserCollectionVo> userCollectionPageList(String userId) {
        return userCollectionMapper.userCollectionPageList(userId);
    }



    /**
     * 根据中间表编号查询 用户得藏品详情
     * @param id
     * @return
     */

    @Override
    public UserCollectionVo userCollectionById(String id) {
        return userCollectionMapper.userCollectionById(id);
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
        return Objects.nonNull(getOne(Wrappers.<UserCollection>lambdaQuery().eq(UserCollection::getIsExist,USE_EXIST.getCode()).eq(UserCollection::getId,id).eq(UserCollection::getUserId,userId).eq(UserCollection::getIsLink,OK_LINK.getCode())));
    }

    /**
     * 转让 藏品
     * @param tranUserId
     * @param toUserId
     * @param buiId
     */
    @Override
    public void tranCollection(String tranUserId, String toUserId, String buiId) {
        UserCollection userCollection = getOne(Wrappers.<UserCollection>lambdaQuery().eq(UserCollection::getId, buiId).eq(UserCollection::getUserId, tranUserId).eq(UserCollection::getIsLink, OK_LINK.getCode()));
        String format = StrUtil.format("{}:赠送获得该藏品!", userCollection.getCollectionName());
        String formatTran = StrUtil.format("{}:藏品被赠送!",userCollection.getCollectionName());
        //原始拥有者的绑定关系 解绑
        userCollection.setIsExist(NOT_EXIST.getCode());
        updateById(userCollection);
        // 增加日志 ...................
        logsService.saveLogs(
                LogInfoDto.builder().jsonTxt(format).buiId(toUserId).modelType(COLLECTION_MODEL_TYPE).isType(PULL_SOURCE).formInfo(Integer.valueOf(1).toString()).build()
                ,LogInfoDto.builder().jsonTxt(formatTran).buiId(tranUserId).modelType(COLLECTION_MODEL_TYPE).isType(POLL_SOURCE).formInfo(Integer.valueOf(1).toString()).build()
        );
        // 此接口更改为转赠链接口
        //bindCollection(toUserId,userCollection.getCollectionId(),userCollection.getCollectionName(),format,Integer.valueOf(1));
        tranCollection(tranUserId,toUserId,userCollection,format,formatTran);


        msgService.saveMsgThis(MsgThisDto.builder().userId(tranUserId).msgForm(formatTran).msgTitle(formatTran).build());
        msgService.saveMsgThis(MsgThisDto.builder().userId(toUserId).msgForm(format).msgTitle(format).build());
        msgService.saveCommMsg(MsgCommDto.builder().msgTitle(format).msgForm(format).build());

    }

    /**
     * 隐藏对应藏品
     * @param buiId
     * @param userId
     * @param info
     */
    @Override
    public String hideUserCollection(String buiId, String userId, String info) {
        UserCollection userCollection = getOne(Wrappers.<UserCollection>lambdaQuery().eq(UserCollection::getIsExist, USE_EXIST.getCode()).eq(UserCollection::getId, buiId).eq(UserCollection::getUserId, userId).eq(UserCollection::getIsLink, OK_LINK));
        Assert.isTrue(Objects.nonNull(userCollection),"藏品有误,检查藏品是否上链!");
        userCollection.setIsExist(NOT_EXIST.getCode());
        userCollection.updateD(userId);
        userCollection.setSourceInfo(StrUtil.join("\n", userCollection.getSourceInfo(),info));
        updateById(userCollection);
        return userCollection.getCollectionId();
    }


    /**
     * 重新上链
     * @param userId
     * @param userCollectionId
     */
    @Override
    public void resetUpLink(String userId, String userCollectionId) {
        UserCollection userCollection = getOne(Wrappers.<UserCollection>lambdaQuery().eq(UserCollection::getUserId,userId).eq(UserCollection::getId,userCollectionId));
        Assert.isTrue(Objects.nonNull(userCollection) && NOT_LINK.getCode().equals(userCollection.getIsLink()),"藏品信息有误,请核实!");
        BigDecimal realPrice = collectionServiceObjectFactory.getObject().getById(userCollection.getCollectionId()).getRealPrice();

        myChainService.accountCollectionUp(CallCommitDto.builder()
                        .userCollectionId(userCollection.getId())
                        .artId(userCollection.getLinkAddr())
                        .artName(userCollection.getCollectionName())
                        .artSize("80")
                        .location(userCollection.getSourceInfo())
                        .price(realPrice.toString())
                        .date(userCollection.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy MM dd")))
                        .sellway(userCollection.getSourceInfo())
                        .owner(userCollection.getUserId())
                        .build(), (hash)->{
                    userCollection.setIsLink(OK_LINK.getCode());
                    userCollection.setRealCompany("蚂蚁链");
                    // 编号特殊生成
                    userCollection.setCollectionNumber(StrUtil.format("CNT_{}", IdUtil.nanoId()));
                    //userCollection.setLinkAddr(hash);
                    userCollection.setCollectionHash(hash);
                    userCollection.updateD(userCollection.getUserId());
                    updateById(userCollection);
                    // TODO 会有个 bug 就是如果是转赠失败的话，那么统一上链处理，流转记录得不到记录。
    });

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
