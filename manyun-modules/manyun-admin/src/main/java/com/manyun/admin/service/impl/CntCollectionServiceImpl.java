package com.manyun.admin.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.manyun.admin.domain.*;
import com.manyun.admin.domain.dto.AirdropDto;
import com.manyun.admin.domain.dto.CntCollectionAlterCombineDto;
import com.manyun.admin.domain.query.CollectionQuery;
import com.manyun.admin.domain.query.UserMoneyQuery;
import com.manyun.admin.domain.vo.*;
import com.manyun.admin.mapper.*;
import com.manyun.common.core.constant.BusinessConstants;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.utils.DateUtils;
import com.manyun.common.core.utils.StringUtils;
import com.manyun.common.core.utils.uuid.IdUtils;
import com.manyun.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manyun.admin.service.ICntCollectionService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 藏品Service业务层处理
 *
 * @author yanwei
 * @date 2022-07-14
 */
@Service
public class CntCollectionServiceImpl implements ICntCollectionService
{
    @Autowired
    private CntCollectionMapper cntCollectionMapper;

    @Autowired
    private CntMediaMapper cntMediaMapper;

    @Autowired
    private CntCollectionInfoMapper collectionInfoMapper;

    @Autowired
    private CntCollectionLableMapper cntCollectionLableMapper;

    @Autowired
    private CntUserMapper cntUserMapper;

    @Autowired
    private CntUserCollectionMapper userCollectionMapper;

    /**
     * 查询藏品
     *
     * @param id 藏品主键
     * @return 藏品
     */
    @Override
    public CntCollectionDetailsVo selectCntCollectionById(String id)
    {
        CntCollectionDetailsVo cntCollectionDetailsVo = cntCollectionMapper.selectCntCollectionDetailsById(id);
        CntCollectionLable cntCollectionLable=new CntCollectionLable();
        cntCollectionLable.setCollectionId(id);
        cntCollectionDetailsVo.setLableIds( cntCollectionLableMapper.selectCntCollectionLableList(cntCollectionLable).stream().map(CntCollectionLable::getLableId).collect(Collectors.toList()));
        cntCollectionDetailsVo.setMediaVos(cntMediaMapper.initMediaVos(id,BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE));
        return cntCollectionDetailsVo;
    }

    /**
     * 查询藏品列表
     *
     * @param collectionQuery
     * @return 藏品
     */
    @Override
    public List<CntCollectionVo> selectCntCollectionList(CollectionQuery collectionQuery)
    {
        return cntCollectionMapper.selectSearchCollectionList(collectionQuery).stream().map(m ->{
            CntCollectionVo cntCollectionVo=new CntCollectionVo();
            BeanUtil.copyProperties(m,cntCollectionVo);
            cntCollectionVo.setMediaVos(cntMediaMapper.initMediaVos(m.getId(), BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE));
            return cntCollectionVo;
        }).collect(Collectors.toList());
    }

    /**
     * 新增藏品
     *
     * @param collectionAlterCombineDto
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertCntCollection(CntCollectionAlterCombineDto collectionAlterCombineDto)
    {
        //藏品
        String idStr = IdUtils.getSnowflakeNextIdStr();
        CntCollectionAlterVo collectionAlterVo = collectionAlterCombineDto.getCntCollectionAlterVo();
        Assert.isTrue(Objects.nonNull(collectionAlterVo),"新增藏品失败");
        CntCollection cntCollection=new CntCollection();
        BeanUtil.copyProperties(collectionAlterVo,cntCollection);
        cntCollection.setId(idStr);
        cntCollection.setCreatedBy(SecurityUtils.getUsername());
        cntCollection.setCreatedTime(DateUtils.getNowDate());
        int collection = cntCollectionMapper.insertCntCollection(cntCollection);
        if(collection==0){
            return 0;
        }
        //藏品详情
        CntCollectionInfoAlterVo collectionInfoAlterVo = collectionAlterCombineDto.getCntCollectionInfoAlterVo();
        if(Objects.nonNull(collectionInfoAlterVo)){
            CntCollectionInfo cntCollectionInfo=new CntCollectionInfo();
            BeanUtil.copyProperties(collectionInfoAlterVo,cntCollectionInfo);
            cntCollectionInfo.setId(IdUtils.getSnowflakeNextIdStr());
            cntCollectionInfo.setCollectionId(idStr);
            cntCollectionInfo.setCreatedBy(SecurityUtils.getUsername());
            cntCollectionInfo.setCreatedTime(DateUtils.getNowDate());
            collectionInfoMapper.insertCntCollectionInfo(cntCollectionInfo);
        }
        //标签
        CntLableAlterVo cntLableAlterVo = collectionAlterCombineDto.getCntLableAlterVo();
        if(Objects.nonNull(cntLableAlterVo)){
            String lableIds = cntLableAlterVo.getLableIds();
            if(StringUtils.isNotBlank(lableIds)){
                String[] arr = lableIds.split(",");
                List<CntCollectionLable> cntCollectionLables =  Arrays.asList(arr).stream().map(m -> {
                    CntCollectionLable cntCollectionLable=new CntCollectionLable();
                    cntCollectionLable.setId(IdUtils.getSnowflakeNextIdStr());
                    cntCollectionLable.setCollectionId(idStr);
                    cntCollectionLable.setLableId(m);
                    cntCollectionLable.setCreatedBy(SecurityUtils.getUsername());
                    cntCollectionLable.setCreatedTime(DateUtils.getNowDate());
                    return cntCollectionLable;
                }).collect(Collectors.toList());
                cntCollectionLableMapper.insertCntCollectionLables(cntCollectionLables);
            }
        }
        //图片
        MediaAlterVo mediaAlterVo = collectionAlterCombineDto.getMediaAlterVo();
        if(Objects.nonNull(mediaAlterVo)){
            CntMedia cntMedia=new CntMedia();
            cntMedia.setId(IdUtils.getSnowflakeNextIdStr());
            cntMedia.setBuiId(idStr);
            cntMedia.setModelType(BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE);
            cntMedia.setMediaUrl(mediaAlterVo.getImg());
            cntMedia.setMediaType(BusinessConstants.ModelTypeConstant.COLLECTION_TAYPE.toString());
            cntMedia.setCreatedBy(SecurityUtils.getUsername());
            cntMedia.setCreatedTime(DateUtils.getNowDate());
            cntMediaMapper.insertCntMedia(cntMedia);
        }
        return 1;
     }

    /**
     * 修改藏品
     *
     * @param collectionAlterCombineDto
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateCntCollection(CntCollectionAlterCombineDto collectionAlterCombineDto)
    {
        //藏品
        CntCollectionAlterVo collectionAlterVo = collectionAlterCombineDto.getCntCollectionAlterVo();
        Assert.isTrue(Objects.nonNull(collectionAlterVo),"修改藏品失败");
        String collectionId = collectionAlterVo.getId();
        Assert.isTrue(StringUtils.isNotBlank(collectionId),"缺失必要参数");
        CntCollection cntCollection=new CntCollection();
        BeanUtil.copyProperties(collectionAlterVo,cntCollection);
        cntCollection.setUpdatedBy(SecurityUtils.getUsername());
        cntCollection.setUpdatedTime(DateUtils.getNowDate());
        int collection = cntCollectionMapper.updateCntCollection(cntCollection);
        if(collection==0){
            return 0;
        }
        //藏品详情
        CntCollectionInfoAlterVo collectionInfoAlterVo = collectionAlterCombineDto.getCntCollectionInfoAlterVo();
        if(Objects.nonNull(collectionInfoAlterVo)){
            List<CntCollectionInfo> cntCollectionInfos = collectionInfoMapper.selectCntCollectionInfoList(Builder.of(CntCollectionInfo::new).with(CntCollectionInfo::setCollectionId, collectionId).build());
            if(cntCollectionInfos.size()==0){
                CntCollectionInfo cntCollectionInfo=new CntCollectionInfo();
                BeanUtil.copyProperties(collectionInfoAlterVo,cntCollectionInfo);
                cntCollectionInfo.setId(IdUtils.getSnowflakeNextIdStr());
                cntCollectionInfo.setCollectionId(collectionId);
                cntCollectionInfo.setCreatedBy(SecurityUtils.getUsername());
                cntCollectionInfo.setCreatedTime(DateUtils.getNowDate());
                collectionInfoMapper.insertCntCollectionInfo(cntCollectionInfo);
            }else {
                collectionInfoMapper.updateCntCollectionInfo(
                        Builder
                                .of(CntCollectionInfo::new)
                                .with(CntCollectionInfo::setId,cntCollectionInfos.get(0).getId())
                                .with(CntCollectionInfo::setCustomerTail,collectionInfoAlterVo.getCustomerTail())
                                .with(CntCollectionInfo::setPublishOther,collectionInfoAlterVo.getPublishOther())
                                .with(CntCollectionInfo::setUpdatedBy,SecurityUtils.getUsername())
                                .with(CntCollectionInfo::setUpdatedTime,DateUtils.getNowDate())
                                .build()
                );
            }
        }
        //标签
        CntLableAlterVo cntLableAlterVo = collectionAlterCombineDto.getCntLableAlterVo();
        String lableIds = cntLableAlterVo.getLableIds();
        if(Objects.nonNull(collectionInfoAlterVo)){
            if( StringUtils.isNotBlank(lableIds)){
                cntCollectionLableMapper.deleteCntCollectionLableById(null,collectionId);
                String[] arr = lableIds.split(",");
                List<CntCollectionLable> cntCollectionLables =  Arrays.asList(arr).stream().map(m -> {
                    CntCollectionLable cntCollectionLable=new CntCollectionLable();
                    cntCollectionLable.setId(IdUtils.getSnowflakeNextIdStr());
                    cntCollectionLable.setCollectionId(collectionId);
                    cntCollectionLable.setLableId(m);
                    cntCollectionLable.setCreatedBy(SecurityUtils.getUsername());
                    cntCollectionLable.setCreatedTime(DateUtils.getNowDate());
                    return cntCollectionLable;
                }).collect(Collectors.toList());
                cntCollectionLableMapper.insertCntCollectionLables(cntCollectionLables);
            }else {
                cntCollectionLableMapper.deleteCntCollectionLableById(null,collectionId);
            }
        }
        //图片
        MediaAlterVo mediaAlterVo = collectionAlterCombineDto.getMediaAlterVo();
        if(Objects.nonNull(mediaAlterVo)){
            List<MediaVo> mediaVos = cntMediaMapper.initMediaVos(collectionId, BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE);
            if(mediaVos.size()==0){
                CntMedia cntMedia=new CntMedia();
                cntMedia.setId(IdUtils.getSnowflakeNextIdStr());
                cntMedia.setBuiId(collectionId);
                cntMedia.setModelType(BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE);
                cntMedia.setMediaUrl(mediaAlterVo.getImg());
                cntMedia.setMediaType(BusinessConstants.ModelTypeConstant.COLLECTION_TAYPE.toString());
                cntMedia.setCreatedBy(SecurityUtils.getUsername());
                cntMedia.setCreatedTime(DateUtils.getNowDate());
                cntMediaMapper.insertCntMedia(cntMedia);
            }else {
                cntMediaMapper.updateCntMedia(
                        Builder.of(CntMedia::new)
                        .with(CntMedia::setId,mediaVos.get(0).getId())
                        .with(CntMedia::setMediaUrl,mediaAlterVo.getImg())
                        .with(CntMedia::setUpdatedBy,SecurityUtils.getUsername())
                        .with(CntMedia::setUpdatedTime,DateUtils.getNowDate())
                        .build()
                );
            }
        }
        return 1;
    }

    /**
     * 批量删除藏品
     *
     * @param ids 需要删除的藏品主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteCntCollectionByIds(String[] ids)
    {
        if(ids.length==0){
            return 0;
        }
        int collection = cntCollectionMapper.deleteCntCollectionByIds(ids);
        if(collection==0){
            return 0;
        }else {
            collectionInfoMapper.deleteCntCollectionInfoByCollectionIds(ids);
            cntCollectionLableMapper.deleteCntCollectionLableByCollectionIds(ids);
            cntMediaMapper.deleteCntMediaByCollectionIds(ids);
        }
        return 1;
    }

    /***
     * 空投
     * @param airdropDto 空投请求参数
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int airdrop(AirdropDto airdropDto) {
        //验证用户
        List<CntUser> cntUsers = cntUserMapper.selectCntUserList(Builder.of(CntUser::new).with(CntUser::setPhone, airdropDto.getPhone()).build());
        Assert.isTrue(cntUsers.size()>0,"用户不存在!");
        CntCollection collection = cntCollectionMapper.selectCntCollectionById(airdropDto.getCollectionId());
        Assert.isTrue(Objects.nonNull(collection),"藏品不存在!");
        String collectionId = collection.getId();
        Long selfBalance = collection.getSelfBalance();
        Long balance = collection.getBalance();
        Assert.isFalse(selfBalance>=balance,"已售空!");
        //扣减库存
        cntCollectionMapper.updateCntCollection(
                Builder
                        .of(CntCollection::new)
                        .with(CntCollection::setId,collectionId)
                        .with(CntCollection::setSelfBalance,(selfBalance+1))
                        .with(CntCollection::setBalance,(balance-1))
                        .build()
        );
        //用户藏品信息未完善 待上链
        return userCollectionMapper.insertCntUserCollection(
                Builder
                        .of(CntUserCollection::new)
                        .with(CntUserCollection::setId,IdUtils.getSnowflakeNextIdStr())
                        .with(CntUserCollection::setUserId,cntUsers.get(0).getUserId())
                        .with(CntUserCollection::setCollectionId,collection.getId())
                        .with(CntUserCollection::setCollectionName,collection.getCollectionName())
                        .build()
        );
    }

}
