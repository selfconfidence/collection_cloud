package com.manyun.admin.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.manyun.admin.domain.*;
import com.manyun.admin.domain.vo.*;
import com.manyun.admin.mapper.*;
import com.manyun.common.core.constant.BusinessConstants;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.enums.CateType;
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
    private CntCateMapper cntCateMapper;

    @Autowired
    private CnfCreationdMapper cnfCreationdMapper;

    @Autowired
    private CntLableMapper cntLableMapper;

    @Autowired
    private CntCollectionInfoMapper collectionInfoMapper;

    @Autowired
    private CntCollectionLableMapper cntCollectionLableMapper;

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
     * @param cntCollection 藏品
     * @return 藏品
     */
    @Override
    public List<CntCollectionVo> selectCntCollectionList(CntCollection cntCollection)
    {
        return cntCollectionMapper.selectCntCollectionList(cntCollection).stream().map(m ->{
            CntCollectionVo cntCollectionVo=new CntCollectionVo();
            BeanUtil.copyProperties(m,cntCollectionVo);
            cntCollectionVo.setMediaVos(cntMediaMapper.initMediaVos(m.getId(), BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE));
            return cntCollectionVo;
        }).collect(Collectors.toList());
    }

    /**
     * 新增藏品
     *
     * @param collectionAlterCombineVo 藏品
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertCntCollection(CntCollectionAlterCombineVo collectionAlterCombineVo)
    {
        //藏品
        String idStr = IdUtils.getSnowflakeNextIdStr();
        CntCollectionAlterVo collectionAlterVo = collectionAlterCombineVo.getCntCollectionAlterVo();
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
        CntCollectionInfoAlterVo collectionInfoAlterVo = collectionAlterCombineVo.getCntCollectionInfoAlterVo();
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
        CntLableAlterVo cntLableAlterVo = collectionAlterCombineVo.getCntLableAlterVo();
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
        MediaAlterVo mediaAlterVo = collectionAlterCombineVo.getMediaAlterVo();
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
     * @param collectionAlterCombineVo 藏品
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateCntCollection(CntCollectionAlterCombineVo collectionAlterCombineVo)
    {
        //藏品
        CntCollectionAlterVo collectionAlterVo = collectionAlterCombineVo.getCntCollectionAlterVo();
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
        CntCollectionInfoAlterVo collectionInfoAlterVo = collectionAlterCombineVo.getCntCollectionInfoAlterVo();
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
        CntLableAlterVo cntLableAlterVo = collectionAlterCombineVo.getCntLableAlterVo();
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
        MediaAlterVo mediaAlterVo = collectionAlterCombineVo.getMediaAlterVo();
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
     *  查询藏品分类下拉框
     * @return
     */
    @Override
    public List<CollectionCateDictVo> collectionCateDict() {
        CntCate cntCate=new CntCate();
        cntCate.setCateType(Long.valueOf(CateType.COLLECTION_CATE.getCode()));
        return cntCateMapper.selectCntCateList(cntCate).stream().map(m ->{
            CollectionCateDictVo collectionCateDictVo=new CollectionCateDictVo();
            BeanUtil.copyProperties(m,collectionCateDictVo);
            return collectionCateDictVo;
        }).collect(Collectors.toList());
    }

    /***
     *  查询创作者下拉框
     * @return
     */
    @Override
    public List<CollectionCreationdDictVo> collectionCreationdDict() {
        return cnfCreationdMapper.selectCnfCreationdList(new CnfCreationd()).stream().map(m ->{
            CollectionCreationdDictVo collectionCreationdDictVo=new CollectionCreationdDictVo();
            BeanUtil.copyProperties(m,collectionCreationdDictVo);
            return collectionCreationdDictVo;
        }).collect(Collectors.toList());
    }

    /***
     *  查询标签下拉框
     * @return
     */
    @Override
    public List<CollectionLableDictVo> collectionLableDict() {
        return cntLableMapper.selectCntLableList(new CntLable()).stream().map(m ->{
            CollectionLableDictVo collectionLableDictVo=new CollectionLableDictVo();
            BeanUtil.copyProperties(m,collectionLableDictVo);
            return collectionLableDictVo;
        }).collect(Collectors.toList());
    }

}
